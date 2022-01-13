package furhatos.app.fruitseller.flow

import furhatos.app.fruitseller.nlu.*
import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.util.Language

val Start : State = state(Interaction) {

    onEntry {
        random(
                {furhat.say("Hello there!")},
                {furhat.say("Welcome!")},
                {furhat.say("Hi there!")}
        )
        //goto(TakingOrder)
        goto(HearCondition)
    }
}

val Options = state(Interaction) {

    onResponse<ContactDoctor> {
        furhat.say ("Okay, let me see if I can get you in touch with a human doctor...")
    }

    onResponse<BuyFruit> {
        val fruits = it.intent.fruits
        if (fruits != null) {
            goto(OrderReceived(fruits))
        }
        else {
            propagate()
        }
    }

    onResponse<RequestOptions> {
        furhat.say("We have ${Fruit().getEnum(Language.ENGLISH_US).joinToString(", ")}")
        furhat.ask("Do you want some?")
    }

    onResponse<RequestPurpose> {
        //furhat.say("Welcome to my fruit stand, my name is furhat, I'm but a simple robot selling simple fruits to travelers")
        //furhat.ask("Do you want to buy some of my delicious fruits?")
        random(
                { furhat.say("My name is Furhat, I'm a social robot with the goal of making your experience better and more interactive") },
                { furhat.say("You can call me Furhat, I'm here to assist you in your medical needs") }
        )
        furhat.ask("Now..., is there anything I can help you with?")
    }

    onResponse<Yes> {
        random(
                { furhat.ask("What kind of fruit do you want?") },
                { furhat.ask("What type of fruit?") }
        )
    }
}

val TakingOrder = state(parent = Options) {
    onEntry {
        random(
                { furhat.ask("How about some fruits?") },
                { furhat.ask("Do you want some fruits?") }
        )
    }

    onResponse<No> {
        furhat.say("Okay, if you're feeling good that's fantastic. Have a splendid day!")
        goto(Idle)
    }
}

val HearCondition = state(parent = Options) {
    onEntry {
        random(
                {furhat.ask("How are you feeling today?")},
                {furhat.ask("How can I help you today?")},
                {furhat.ask("How may I assist you?")}
        )
    }
    onResponse<GiveSymptoms> {
        furhat.say("Okay, that's not good. Lets see if I can assist you with that!")
        goto(Idle)
    }

    onResponse<No> {
        furhat.say("Okay, if you're feeling good that's fantastic. Have a splendid day!")
        goto(Idle)
    }
}

fun OrderReceived(fruitList: FruitList) : State = state(Options) {
    onEntry {
        furhat.say("${fruitList.text}, what a lovely choice!")
        fruitList.list.forEach {
            users.current.order.fruits.list.add(it)
        }
        furhat.ask("Anything else?")
    }

    onReentry {
        furhat.ask("Did you want something else?")
    }

    onResponse<No> {
        furhat.say("Okay, here is your order of ${users.current.order.fruits}.")
        goto(ConfirmOrder)
    }
}

val ConfirmOrder = state(parent=Options) {
    onEntry {
        furhat.ask("Are you sure you want this order?")
    }

    onResponse<Yes> {
        furhat.say("You are done :)")
        goto(Idle)
    }
    onResponse<No> {
        furhat.say("Lets go back then!")
        goto(TakingOrder)
    }
}