package furhatos.app.fruitseller.nlu

import furhatos.nlu.EnumEntity
import furhatos.nlu.Intent
import furhatos.nlu.ListEntity
import furhatos.util.Language
import furhatos.nlu.ComplexEnumEntity
import furhatos.nlu.common.Number

/*class Fruit : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("banana", "orange", "apple", "cherimoya")
    }
}*/

class HeadPosition : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("forehead", "eyes", "neck", "Shoulder", "left side", "right side", "top", "back", "face", "front")
    }
}

class PainQuality : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("Pressing", "tightening", "pulsing", "throbbing", "banging", "Variable", "sharp", "burning")
    }
}

class PainIntensity : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("mild", "moderate", "severe")
    }
}

class EyeSymptoms : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("red eye", "watery eye", "red eyes", "watery eye", "runny nose","facial sweating")
    }
}

class Time : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("minutes", "hours", "days")
    }
}

/*class BuyFruit(var fruits : FruitList? = null) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@fruits", "I want @fruits", "I would like @fruits", "I want to buy @fruits")
    }
}*/

class DescribeHeadPositions(var position : HeadPositionList? = null) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@position", "I feel it in my @position", "I have a pain in my @position", "mostly in my @position", "I feel it behind my @position")
    }
}

class DescribePainQuality(var position : PainQualityList? = null) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@position", "It is @position", "It feels like it is @position", "mostly @position", "It feels @position")
    }
}

class DescribePainIntensity(var position : PainIntensityList? = null) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@position", "It is @position", "It feels like it is @position", "mostly @position", "It feels @position")
    }
}

class DescribeOtherSymptoms : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("Yes", "I feel sensitive to light", "I feel sensitive to sounds", "I am sensitive to sounds", "I am sensitive to light")
    }
}

class DescribeEyeSymptoms(var position : EyeSymptoms? = null) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("yes", "I have a @position", "I have @position")
    }
}

class DescribeTime(var position : TimeList? = null) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@position", "It lasts @position", "I have it for @position", "normally @position", "around @position")
    }
}

/*class RequestOptions: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("What options do you have?",
                "What fruits do you have?",
                "What are the alternatives?",
                "What do you have?")
    }
}*/


class DescribeHeadMoreDetails: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("I feel it in my head", "head")
    }
}

class AddMore: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("I want to add some more",
            "no",
            "Could I add some more?",
            "I'm not done")
    }
}

class RequestPurpose: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("who are you?",
                "what is this?",
                "where am I?",
                "what is your purpose?")
    }
}

class HeadPositionList : ListEntity<HeadPositionStack>()

class HeadPositionStack(
    val position : HeadPosition? = null) : ComplexEnumEntity() {

    override fun getEnum(lang: Language): List<String> {
        return listOf("@position")
    }

    override fun toText(): String {
        return generate("$position")
    }
}

class PainQualityList : ListEntity<PainQualityStack>()

class PainQualityStack(
    val position : PainQuality? = null) : ComplexEnumEntity() {

    override fun getEnum(lang: Language): List<String> {
        return listOf("@position")
    }

    override fun toText(): String {
        return generate("$position")
    }
}

class PainIntensityList : ListEntity<PainIntensityStack>()

class PainIntensityStack(
    val position : PainIntensity? = null) : ComplexEnumEntity() {

    override fun getEnum(lang: Language): List<String> {
        return listOf("@position")
    }

    override fun toText(): String {
        return generate("$position")
    }
}

class TimeList : ListEntity<TimeStack>()

class TimeStack(
    val numb : Number? = Number(1),
    val position : Time? = null) : ComplexEnumEntity() {

    override fun getEnum(lang: Language): List<String> {
        return listOf("@numb @position", "@position")
    }

    fun getTime(): String{
        return numb.toString()
    }

    override fun toText(): String {
        return generate("$numb $position")
    }
}
/*class FruitList : ListEntity<QuantifiedFruit>()

class QuantifiedFruit(
        val count : Number? = Number(1),
        val fruit : Fruit? = null) : ComplexEnumEntity() {

    override fun getEnum(lang: Language): List<String> {
        return listOf("@count @fruit", "@fruit")
    }

    override fun toText(): String {
        return generate("$count $fruit")
    }
}*/

