package api.dto.requests.recipe

import bl.entities.PFC
import kotlinx.serialization.Serializable

@Serializable
data class PFC(var protein: Int, var fat: Int, var carbon: Int) {
    fun toBLEntity(): PFC {
        return PFC(protein, fat, carbon)
    }
}
