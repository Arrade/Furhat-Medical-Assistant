package furhatos.app.fruitseller.flow

import furhatos.flow.kotlin.*
import furhatos.records.User
import furhatos.util.*
import org.omg.CORBA.Object

val Idle: State = state {

    init {
        furhat.setVoice(Language.ENGLISH_US, Gender.MALE)
        if (users.count > 0) {
            furhat.attend(users.random)
            goto(Start)
        }
    }

    onEntry {
        furhat.attendNobody()
    }

    onUserEnter {
        furhat.attend(it)
        goto(Start)
    }
}

var k: User? = null

val Interaction: State = state {

    onUserLeave(instant = true) {
        if (users.count > 0) {
            if (it == users.current) {
                furhat.attend(users.other)
                goto(Start)
            } else {
                furhat.glance(it)
            }
        } else {

            if (k != null) {
                furhat.attend(k!!)
                goto(Start)
            } else {
                goto(Idle)
            }

        }
    }

    onUserEnter {
        if (users.count > 1) {
            val u = users.current
            k = it
            furhat.attend(it)
            furhat.say("wait just a moment")
            furhat.attend(u)
            reentry()
        }
    }
}