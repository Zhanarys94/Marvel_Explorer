package kz.zhanarys.domain.models

data class ComicItemModel(
    val id: Int,
    val title: String,
    val format: String,
    val imageUrl: String,
    val imageExtension: String,
) {
}