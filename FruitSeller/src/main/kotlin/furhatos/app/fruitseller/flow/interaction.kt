package furhatos.app.fruitseller.flow

import furhatos.app.fruitseller.nlu.*
import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.util.Language

val Start : State = state(Interaction) {

    onEntry {
        random(
                {furhat.say("Hi there")},
                {furhat.say("Oh, hello there general kenobi")}
        )

        goto(TakingOrder)
    }
}

val Options = state(Interaction) {

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
        furhat.say("Welcome to my fruit stand, my name is furhat, I'm but a simple robot selling simple fruits to travelers")
        furhat.ask("Do you want to buy some of my delicious fruits?")
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
        furhat.say("Okay, that's a shame. Have a splendid day!")
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