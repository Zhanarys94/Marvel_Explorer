package kz.zhanarys.data.repositories.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kz.zhanarys.data.repositories.local.CharacterEntity
import kz.zhanarys.domain.models.CharacterEntityModel
import kz.zhanarys.domain.models.CharacterItemModel


//Marvel API documentation: https://developer.marvel.com/docs
//CharacterDataWrapper {
//    code (int, optional): The HTTP status code of the returned result.,
//    status (string, optional): A string description of the call status.,
//    copyright (string, optional): The copyright notice for the returned result.,
//    attributionText (string, optional): The attribution notice for this result. Please display either this notice or the contents of the attributionHTML field on all screens which contain data from the Marvel Comics API.,
//    attributionHTML (string, optional): An HTML representation of the attribution notice for this result. Please display either this notice or the contents of the attributionText field on all screens which contain data from the Marvel Comics API.,
//    data (CharacterDataContainer, optional): The results returned by the call.,
//    etag (string, optional): A digest value of the content returned by the call.
//}
//CharacterDataContainer {
//    offset (int, optional): The requested offset (number of skipped results) of the call.,
//    limit (int, optional): The requested result limit.,
//    total (int, optional): The total number of resources available given the current filter set.,
//    count (int, optional): The total number of results returned by this call.,
//    results (Array[Character], optional): The list of characters returned by the call.
//}
//Character {
//    id (int, optional): The unique ID of the character resource.,
//    name (string, optional): The name of the character.,
//    description (string, optional): A short bio or description of the character.,
//    modified (Date, optional): The date the resource was most recently modified.,
//    resourceURI (string, optional): The canonical URL identifier for this resource.,
//    urls (Array[Url], optional): A set of public web site URLs for the resource.,
//    thumbnail (Image, optional): The representative image for this character.,
//    comics (ComicList, optional): A resource list containing comics which feature this character.,
//    stories (StoryList, optional): A resource list of stories in which this character appears.,
//    events (EventList, optional): A resource list of events in which this character appears.,
//    series (SeriesList, optional): A resource list of series in which this character appears.
//Url {
//    type (string, optional): A text identifier for the URL.,
//    url (string, optional): A full URL (including scheme, domain, and path).
//}
//Image {
//    path (string, optional): The directory path of to the image.,
//    extension (string, optional): The file extension for the image.
//}
//ComicList {
//    available (int, optional): The number of total available issues in this list. Will always be greater than or equal to the "returned" value.,
//    returned (int, optional): The number of issues returned in this collection (up to 20).,
//    collectionURI (string, optional): The path to the full list of issues in this collection.,
//    items (Array[ComicSummary], optional): The list of returned issues in this collection.
//}
//ComicSummary {
//    resourceURI (string, optional): The path to the individual comic resource.,
//    name (string, optional): The canonical name of the comic.
//}
//StoryList {
//    available (int, optional): The number of total available stories in this list. Will always be greater than or equal to the "returned" value.,
//    returned (int, optional): The number of stories returned in this collection (up to 20).,
//    collectionURI (string, optional): The path to the full list of stories in this collection.,
//    items (Array[StorySummary], optional): The list of returned stories in this collection.
//}
//StorySummary {
//    resourceURI (string, optional): The path to the individual story resource.,
//    name (string, optional): The canonical name of the story.,
//    type (string, optional): The type of the story (interior or cover).
//}
//EventList {
//    available (int, optional): The number of total available events in this list. Will always be greater than or equal to the "returned" value.,
//    returned (int, optional): The number of events returned in this collection (up to 20).,
//    collectionURI (string, optional): The path to the full list of events in this collection.,
//    items (Array[EventSummary], optional): The list of returned events in this collection.
//}
//EventSummary {
//    resourceURI (string, optional): The path to the individual event resource.,
//    name (string, optional): The name of the event.
//}
//SeriesList {
//    available (int, optional): The number of total available series in this list. Will always be greater than or equal to the "returned" value.,
//    returned (int, optional): The number of series returned in this collection (up to 20).,
//    collectionURI (string, optional): The path to the full list of series in this collection.,
//    items (Array[SeriesSummary], optional): The list of returned series in this collection.
//}
//SeriesSummary {
//    resourceURI (string, optional): The path to the individual series resource.,
//    name (string, optional): The canonical name of the series.
//}

@JsonClass(generateAdapter = true)
data class CharacterDataWrapperDto(
    val code: Int,
    val status: String,
    val data: DataContainerDto,
    val etag: String,
    val copyright: String,
    val attributionText: String,
    val attributionHTML: String
)

@JsonClass(generateAdapter = true)
data class DataContainerDto(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<CharacterDto>
)

@JsonClass(generateAdapter = true)
data class CharacterDto(
    val id: Int,
    val name: String,
    @Json(name = "description") val shortInfo: String,
    val modified: String,
    val resourceURI: String,
    val urls: List<UrlDto>,
    val thumbnail: ImageDto,
    val comics: ComicListDto,
    val stories: StoryListDto,
    val events: EventListDto,
    val series: SeriesListDto
)

fun CharacterDto.toCharacterItemModel(): CharacterItemModel {
    return CharacterItemModel(
        id,
        name,
        thumbnail.path.replace("http", "https"),
        "." + thumbnail.extension,
        shortInfo,
        false
    )
}

fun CharacterDto.toCharacterEntityModel(): CharacterEntityModel {
    return CharacterEntityModel(
        id,
        name,
        thumbnail.path.replace("http", "https"),
        "." + thumbnail.extension,
        shortInfo,
        urls.find { it.type == "detail" }?.url ?: ""
    )
}

@JsonClass(generateAdapter = true)
data class UrlDto(
    val type: String,
    val url: String
)

@JsonClass(generateAdapter = true)
data class ImageDto(
    val path: String,
    val extension: String
)

@JsonClass(generateAdapter = true)
data class ComicListDto(
    val available: Int,
    val returned: Int,
    val collectionURI: String,
    val items: List<ComicSummaryDto>
)

@JsonClass(generateAdapter = true)
data class ComicSummaryDto(
    val resourceURI: String,
    val name: String
)

@JsonClass(generateAdapter = true)
data class StoryListDto(
    val available: Int,
    val returned: Int,
    val collectionURI: String,
    val items: List<StorySummaryDto>
)

@JsonClass(generateAdapter = true)
data class StorySummaryDto(
    val resourceURI: String,
    val name: String,
    val type: String
)

@JsonClass(generateAdapter = true)
data class EventListDto(
    val available: Int,
    val returned: Int,
    val collectionURI: String,
    val items: List<EventSummaryDto>
)

@JsonClass(generateAdapter = true)
data class EventSummaryDto(
    val resourceURI: String,
    val name: String
)

@JsonClass(generateAdapter = true)
data class SeriesListDto(
    val available: Int,
    val returned: Int,
    val collectionURI: String,
    val items: List<SeriesSummaryDto>
)

@JsonClass(generateAdapter = true)
data class SeriesSummaryDto(
    val resourceURI: String,
    val name: String
)