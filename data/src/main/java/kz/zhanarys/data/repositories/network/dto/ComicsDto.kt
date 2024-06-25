package kz.zhanarys.data.repositories.network.dto

import com.squareup.moshi.JsonClass
import kz.zhanarys.domain.models.ComicItemModel

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
