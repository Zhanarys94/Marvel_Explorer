package kz.zhanarys.data.repositories.network.dto

import com.squareup.moshi.JsonClass
import kz.zhanarys.domain.models.ComicItemModel

/*
Comic {
    id (int, optional): The unique ID of the comic resource.,
    digitalId (int, optional): The ID of the digital comic representation of this comic. Will be 0 if the comic is not available digitally.,
    title (string, optional): The canonical title of the comic.,
    issueNumber (double, optional): The number of the issue in the series (will generally be 0 for collection formats).,
    variantDescription (string, optional): If the issue is a variant (e.g. an alternate cover, second printing, or director’s cut), a text description of the variant.,
    description (string, optional): The preferred description of the comic.,
    modified (Date, optional): The date the resource was most recently modified.,
    isbn (string, optional): The ISBN for the comic (generally only populated for collection formats).,
    upc (string, optional): The UPC barcode number for the comic (generally only populated for periodical formats).,
    diamondCode (string, optional): The Diamond code for the comic.,
    ean (string, optional): The EAN barcode for the comic.,
    issn (string, optional): The ISSN barcode for the comic.,
    format (string, optional): The publication format of the comic e.g. comic, hardcover, trade paperback.,
    pageCount (int, optional): The number of story pages in the comic.,
    textObjects (Array[TextObject], optional): A set of descriptive text blurbs for the comic.,
    resourceURI (string, optional): The canonical URL identifier for this resource.,
    urls (Array[Url], optional): A set of public web site URLs for the resource.,
    series (SeriesSummary, optional): A summary representation of the series to which this comic belongs.,
    variants (Array[ComicSummary], optional): A list of variant issues for this comic (includes the "original" issue if the current issue is a variant).,
    collections (Array[ComicSummary], optional): A list of collections which include this comic (will generally be empty if the comic's format is a collection).,
    collectedIssues (Array[ComicSummary], optional): A list of issues collected in this comic (will generally be empty for periodical formats such as "comic" or "magazine").,
    dates (Array[ComicDate], optional): A list of key dates for this comic.,
    prices (Array[ComicPrice], optional): A list of prices for this comic.,
    thumbnail (Image, optional): The representative image for this comic.,
    images (Array[Image], optional): A list of promotional images associated with this comic.,
    creators (CreatorList, optional): A resource list containing the creators associated with this comic.,
    characters (CharacterList, optional): A resource list containing the characters which appear in this comic.,
    stories (StoryList, optional): A resource list containing the stories which appear in this comic.,
    events (EventList, optional): A resource list containing the events in which this comic appears.
}
TextObject {
    type (string, optional): The canonical type of the text object (e.g. solicit text, preview text, etc.).,
    language (string, optional): The IETF language tag denoting the language the text object is written in.,
    text (string, optional): The text.
}
Url {
    type (string, optional): A text identifier for the URL.,
    url (string, optional): A full URL (including scheme, domain, and path).
}
SeriesSummary {
    resourceURI (string, optional): The path to the individual series resource.,
    name (string, optional): The canonical name of the series.
}
ComicSummary {
    resourceURI (string, optional): The path to the individual comic resource.,
    name (string, optional): The canonical name of the comic.
}
ComicDate {
    type (string, optional): A description of the date (e.g. onsale date, FOC date).,
    date (Date, optional): The date.
}
ComicPrice {
    type (string, optional): A description of the price (e.g. print price, digital price).,
    price (float, optional): The price (all prices in USD).
}
Image {
    path (string, optional): The directory path of to the image.,
    extension (string, optional): The file extension for the image.
}
CreatorList {
    available (int, optional): The number of total available creators in this list. Will always be greater than or equal to the "returned" value.,
    returned (int, optional): The number of creators returned in this collection (up to 20).,
    collectionURI (string, optional): The path to the full list of creators in this collection.,
    items (Array[CreatorSummary], optional): The list of returned creators in this collection.
}
CreatorSummary {
    resourceURI (string, optional): The path to the individual creator resource.,
    name (string, optional): The full name of the creator.,
    role (string, optional): The role of the creator in the parent entity.
}
CharacterList {
    available (int, optional): The number of total available characters in this list. Will always be greater than or equal to the "returned" value.,
    returned (int, optional): The number of characters returned in this collection (up to 20).,
    collectionURI (string, optional): The path to the full list of characters in this collection.,
    items (Array[CharacterSummary], optional): The list of returned characters in this collection.
}
CharacterSummary {
    resourceURI (string, optional): The path to the individual character resource.,
    name (string, optional): The full name of the character.,
    role (string, optional): The role of the creator in the parent entity.
}
StoryList {
    available (int, optional): The number of total available stories in this list. Will always be greater than or equal to the "returned" value.,
    returned (int, optional): The number of stories returned in this collection (up to 20).,
    collectionURI (string, optional): The path to the full list of stories in this collection.,
    items (Array[StorySummary], optional): The list of returned stories in this collection.
}
StorySummary {
    resourceURI (string, optional): The path to the individual story resource.,
    name (string, optional): The canonical name of the story.,
    type (string, optional): The type of the story (interior or cover).
}
EventList {
    available (int, optional): The number of total available events in this list. Will always be greater than or equal to the "returned" value.,
    returned (int, optional): The number of events returned in this collection (up to 20).,
    collectionURI (string, optional): The path to the full list of events in this collection.,
    items (Array[EventSummary], optional): The list of returned events in this collection.
}
EventSummary {
    resourceURI (string, optional): The path to the individual event resource.,
    name (string, optional): The name of the event.
}*/
@JsonClass(generateAdapter = true)
data class ComicDataWrapperDto(
    val code: Int,
    val status: String,
    val data: ComicsDataContainerDto,
    val etag: String,
    val copyright: String,
    val attributionText: String,
    val attributionHTML: String
)

@JsonClass(generateAdapter = true)
data class ComicsDataContainerDto(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<ComicDto>
)

@JsonClass(generateAdapter = true)
data class ComicDto(
    val id: Int,
    val digitalId: Int,
    val title: String,
    val issueNumber: Double,
    val variantDescription: String,
    val description: String?,
    val modified: String?,
    val isbn: String?,
    val upc: String?,
    val diamondCode: String?,
    val ean: String?,
    val issn: String?,
    val format: String?,
    val pageCount: Int?,
    val textObjects: List<TextObjectDto?>?,
    val resourceURI: String?,
    val urls: List<UrlDto>?,
    val series: SeriesSummaryDto?,
    val variants: List<ComicSummaryDto?>?,
    val collections: List<ComicSummaryDto?>?,
    val collectedIssues: List<ComicSummaryDto?>?,
    val dates: List<DateDto?>?,
    val prices: List<PriceDto?>?,
    val thumbnail: ImageDto?,
    val images: List<ImageDto?>?,
    val creators: CreatorListDto?,
    val characters: CharacterListDto?,
    val stories: StoryListDto?,
    val events: EventListDto?
)

fun ComicDto.toComicsItemModel(): ComicItemModel {
    return ComicItemModel(
        id,
        title,
        format ?: "Unknown",
        (thumbnail?.path ?: "").replace("http", "https"),
        "." + thumbnail?.extension
    )
}

@JsonClass(generateAdapter = true)
data class TextObjectDto(
    val type: String?,
    val language: String?,
    val text: String?
)

@JsonClass(generateAdapter = true)
data class DateDto(
    val type: String?,
    val date: String?
)

@JsonClass(generateAdapter = true)
data class PriceDto(
    val type: String?,
    val price: Float?
)

@JsonClass(generateAdapter = true)
data class CharacterListDto(
    val available: Int?,
    val returned: Int?,
    val collectionURI: String?,
    val items: List<CharacterSummaryDto?>?
)

@JsonClass(generateAdapter = true)
data class CharacterSummaryDto(
    val resourceURI: String?,
    val name: String?,
    val role: String?
)

@JsonClass(generateAdapter = true)
data class CreatorListDto(
    val available: Int?,
    val returned: Int?,
    val collectionURI: String?,
    val items: List<CreatorSummaryDto?>?
)

@JsonClass(generateAdapter = true)
data class CreatorSummaryDto(
    val resourceURI: String?,
    val name: String?,
    val role: String?
)
