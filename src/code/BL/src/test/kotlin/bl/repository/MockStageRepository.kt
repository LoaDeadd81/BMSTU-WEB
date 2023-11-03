//package bl.repository
//
//import bl.entities.Stage
//
//class MockStageRepository : IStageRepository {
//    override fun create(obj: Stage) {
//        StageMockData += obj
//    }
//
//    override fun read(id: ULong): Stage = StageMockData[id.toInt()]
//
//    override fun update(obj: Stage) {
//        StageMockData[obj.id.toInt()] = obj
//    }
//
//    override fun delete(id: ULong) {
//        StageMockData[id.toInt()].description = "deleted"
//    }
//
//    override fun getAll(): List<Stage> = StageMockData.toList()
//
//    override fun exists(id: ULong): Boolean = StageMockData.find { x -> x.id == id } != null
//}