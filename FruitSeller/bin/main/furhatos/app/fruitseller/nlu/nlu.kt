package furhatos.app.fruitseller.nlu

import furhatos.nlu.EnumEntity
import furhatos.nlu.Intent
import furhatos.nlu.ListEntity
import furhatos.util.Language
import furhatos.nlu.ComplexEnumEntity

import furhatos.nlu.common.Number

class Fruit : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("banana", "orange", "apple", "cherimoya")
    }
}

class BuyFruit(var fruits : FruitList? = null) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@fruits", "I want @fruits", "I would like @fruits", "I want to buy @fruits")
    }
}

class Name(var name : Name? = null) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@name", "My name is @name", "I am @name", "I'm @name", "I'm called @name")
    }
}

class RequestOptions: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("What options do you have?",
                "What fruits do you have?",
                "What are the alternatives?",
                "What do you have?")
    }
}

class GiveSymptoms: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
                "My stomach hurts",
                "There is something bad with my stomach",
                "I have a headache",
                "My head hurts"
        )
    }
}

class ContactDoctor: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
                "I need to see a doctor",
                "I want to see a doctor",
                "Let me see a human doctor",
                "Meet a human"
        )
    }
}

class RequestPurpose: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("who are you?",
                "what is this?",
                "where am I?",
                "what is your purpose?",
                "what are you?",
                "Who are you?"
        )
    }
}

class FruitList : ListEntity<QuantifiedFruit>()

class QuantifiedFruit(
        val count : Number? = Number(1),
        val fruit : Fruit? = null) : ComplexEnumEntity() {

    override fun getEnum(lang: Language): List<String> {
        return listOf("@count @fruit", "@fruit")
    }

    override fun toText(): String {
        return generate("$count $fruit")
    }
}

class Tuesday: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
                "tuesday",
                "Tuesday"
        )
    }
}

class Thursday: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
                "thursday",
                "Thursday"
        )
    }
}

