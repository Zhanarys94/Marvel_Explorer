package kz.zhanarys.data.repositories.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//GET /v1/public/characters/{characterId} This method fetches a single character resource. It is the canonical URI for any character resource provided by the API.
//Server-side applications must pass two parameters in addition to the apikey parameter:
//
//ts - a timestamp (or other long string which can change on a request-by-request basis)
//hash - a md5 digest of the ts parameter, your private key and your public key (e.g. md5(ts+privateKey+publicKey)
//For example, a user with a public key of "1234" and a private key of "abcd" could construct a valid call as follows: http://gateway.marvel.com/v1/public/comics?ts=1&apikey=1234&hash=ffd275c5130566a2916217b101f26150 (the hash value is the md5 digest of 1abcd1234)
interface MarvelApiRest {
    @GET("v1/public/characters")
    suspend fun getCharacters(
        @Query("ts") timestamp: String,
        @Query("apikey") publicKey: String,
        @Query("hash") hash: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): CharacterDataWrapperDto

    @GET("v1/public/characters/{characterId}")
    suspend fun getCharacterById(
        @Path("characterId") characterId: Int,
        @Query("ts") timestamp: String,
        @Query("apikey") publicKey: String,
        @Query("hash") hash: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): CharacterDataWrapperDto

    @GET("v1/public/characters")
    suspend fun getCharacterByNameStartingWith(
        @Query("nameStartsWith") nameStartsWith: String,
        @Query("ts") timestamp: String,
        @Query("apikey") publicKey: String,
        @Query("hash") hash: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): CharacterDataWrapperDto

    @GET("v1/public/characters")
    suspend fun getCharacterByName(
        @Query("name") name: String,
        @Query("ts") timestamp: String,
        @Query("apikey") publicKey: String,
        @Query("hash") hash: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): CharacterDataWrapperDto
}