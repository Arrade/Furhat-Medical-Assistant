package furhatos.app.fruitseller.flow

import furhatos.app.fruitseller.nlu.*
import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.util.Language
import javax.swing.text.html.Option

val Start : State = state(Interaction) {

    onEntry {
        furhat.gesture(Gestures.BigSmile)
        random(
                {furhat.say("Hello there!")},
                {furhat.say("Welcome!")},
                {furhat.say("Hi there!")}
        )
        //furhat.ask("What is your name?")
        //goto(TakingOrder)
        goto(HearCondition)
    }

    onResponse<Name> {
        val name = it.intent.name
        //goto(NameReceived(name))
    }
}

fun NameReceived(name : Name) = state {
    onEntry {
        //furhat.say("Hello \"${name.text}")
    }
}

val Options = state(Interaction) {

    onResponse<ContactDoctor> {
        furhat.say ("Okay I see...")
        furhat.gesture(Gestures.Nod)
        furhat.say("Let me see if I can get you in touch with a human doctor...")
        goto(DirectToDoctor)
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
        goto(Instructions)
    }

    onResponse<Yes> {
        call(AskForStatus)
        //furhat.say("What do you need help with?")
    }

    onResponse<No> {
        furhat.say("Okay, if you're feeling good that's fantastic. Have a splendid day!")
        goto(Idle)
    }
}

val AskForStatus = state {
    onEntry {
        furhat.ask("What do you need help with?")
    }

    onResponse<GiveSymptoms> {
        furhat.say("That sounds like it hurts, let me see if I can help you with that!")
        terminate()
    }

    onResponse {
        terminate()
    }
}

val Instructions = state(parent = Options) {
    onEntry {
        furhat.say("I'll ask you some questions about your headache to help determine the cause")
        furhat.say("If anything is unclear just let me know!")
        furhat.gesture(Gestures.Blink)
        goto(Idle)
    }
}

val DirectToDoctor = state {
    onEntry {
        furhat.say("I can schedule you for a doctor's appointment at 2.30 pm today")
        furhat.ask("Would that work for you?")
    }
    onResponse<No> {
        furhat.say("Lets find another day that works then")
        call(Reschedule)
    }
}

val Reschedule = state {
    onEntry {
        furhat.ask("There is a time slot at 1 pm tomorrow and one at 1 pm on thursday, how does that sound?")
    }
    onResponse<Tuesday> {
        furhat.say("Great! You're now scheduled for a doctor's appointment on Tuesday at 1 pm")
        terminate()
    }

    onResponse<Thursday> {
        furhat.say("Great! You're now scheduled for a doctor's appointment on Thursday at 1 pm")
        terminate()
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