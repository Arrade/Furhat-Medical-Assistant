package furhatos.app.fruitseller.flow

//import furhatos.app.fruitseller.nlu.FruitList
import furhatos.app.fruitseller.nlu.HeadPositionList
import furhatos.app.fruitseller.nlu.PainQualityList
import furhatos.app.fruitseller.nlu.PainIntensityList
import furhatos.app.fruitseller.nlu.TimeList
import furhatos.records.User

/*class FruitData (
        var fruits : FruitList = FruitList()
)*/

class GeneralData (
    var HeadPositions : HeadPositionList = HeadPositionList(),
    var PainQuality : PainQualityList = PainQualityList(),
    var PainIntensity : PainIntensityList = PainIntensityList(),
    var OtherLight : Boolean = false,
    var OtherEye : Boolean = false,
    var PainDuration : TimeList = TimeList(),
    var FrequencyDuration : TimeList = TimeList()
)

/*val User.order : FruitData
    get() = data.getOrPut(FruitData::class.qualifiedName, FruitData())
*/
val User.order : GeneralData
    get() = data.getOrPut(GeneralData::class.qualifiedName, GeneralData())


