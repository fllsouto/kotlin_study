package br.com.cdc.car.interfaces

import br.com.cdc.car.domain.TravelService
import br.com.cdc.car.domain.TravelRequestStatus
import br.com.cdc.car.interfaces.incoming.mapping.TravelRequestMapper
import org.springframework.hateoas.EntityModel
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping(path = ["/travelRequests"], produces = [MediaType.APPLICATION_JSON_VALUE])
class TravelRequestAPI(
    val travelService: TravelService,
    val mapper: TravelRequestMapper
) {


    @PostMapping
    fun makeTravelRequest(@RequestBody travelRequestInput: TravelRequestInput): EntityModel<TravelRequestOutput> {
        val travelRequest = travelService.saveTravelRequest(mapper.map(travelRequestInput))
        val output = mapper.map(travelRequest)
        return mapper.buildOutputModel(travelRequest, output)
    }

    @GetMapping("/nearby")
    fun listNearbyRequests(@RequestParam currentAddress: String): List<EntityModel<TravelRequestOutput>> {
        val requests = travelService.listNearbyTravelRequests(currentAddress)
        return mapper.buildOutputModel(requests)
    }
}

data class TravelRequestInput(
    val passengerId: Long,
    val origin: String,
    val destination: String
)
data class TravelRequestOutput(
    val id: Long,
    val origin: String,
    val destination: String,
    val status: TravelRequestStatus,
    val creationDate: LocalDateTime
)

