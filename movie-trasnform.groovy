import groovy.json.JsonSlurper
import groovy.json.JsonOutput


def jsonSlurper = new JsonSlurper()
def movie = jsonSlurper.parseText(new String(payload))


def connection = new URL( "https://imdb-data-searching.p.rapidapi.com/om?i=${movie.id}")
                 .openConnection() as HttpURLConnection

connection.setRequestProperty( 'x-rapidapi-host', 'imdb-data-searching.p.rapidapi.com' )
connection.setRequestProperty( 'x-rapidapi-key', 'Z0wkH5vvG5mshvK50pSLYEeYnTpDp1L8yLFjsneOiq7pYrZjXs')  //<-- Change Me
connection.setRequestProperty( 'Accept', 'application/json' )
connection.setRequestProperty( 'Content-Type', 'application/json')

if ( connection.responseCode == 200 ) {
    
    def imdb = connection.inputStream.withCloseable { inStream ->
        new JsonSlurper().parse( inStream as InputStream )
    }
    
    movie.imdb = [ "rating": imdb.imdbRating, "ratingCount": imdb.imdbVotes ]    

} else {
    println connection.responseCode + ": " + connection.inputStream.text
}

JsonOutput.toJson(movie)
