package api.dto.responces.pfc

import bl.entities.PFC
import kotlinx.serialization.Serializable

@Serializable
data class PFCResponse(var protein: Int, var fat: Int, var carbon: Int) {
    constructor(pfc: PFC) : this(pfc.protein, pfc.fat, pfc.protein)
}
