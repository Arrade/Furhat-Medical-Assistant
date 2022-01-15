package furhatos.app.fruitseller.flow

import furhatos.app.fruitseller.nlu.*
import furhatos.nlu.common.*
import furhatos.flow.kotlin.*

val Start : State = state(Interaction) {

    onEntry {
        random(
                {furhat.say("Hi and welcome!")}
                //,{furhat.say("Oh, hello there general kenobi")}
        )

        //goto(TakingOrder)
        goto(StartDiagnosis)
    }
}

val Options = state(Interaction) {

    /*onResponse<BuyFruit> {
        val fruits = it.intent.fruits
        if (fruits != null) {
            goto(OrderReceived(fruits))
        }
        else {
            propagate()
        }
    }*/


    /*onResponse<RequestOptions> {
        furhat.say("We have ${Fruit().getEnum(Language.ENGLISH_US).joinToString(", ")}")
        furhat.ask("Do you want some?")
    }*/

    onResponse<RequestPurpose> {
        furhat.say("I am a medical assistant robot designed to help patients with simple head pain diagnosis")
        reentry()
    }

}

/*val TakingOrder = state(parent = Options) {
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
}*/

val StartDiagnosis = state(parent = Options) {
    onEntry {
        random(
            { furhat.ask("Do you want me to investigate your head pain?") }
        )
    }

    onResponse<Yes> {
        furhat.say("Okay let's begin")
        goto(HeadacheQuestion)
    }

    onResponse<No> {
        furhat.say("Okay, I understand. Have a splendid day and I hope you feel better soon!")
        goto(Idle)
    }

}

val HeadacheQuestion = state(parent = Options) {
    onEntry {
        furhat.ask("Where do you feel the pain? Where in your head, face or neck?")
    }

    onResponse<DescribeHeadPositions> {
        val position = it.intent.position
        if (position != null) {
            goto(headDescriptionReceived(position))
        }
        else {
            propagate()
        }
    }

    onResponse<Yes> {
        furhat.ask("Where do you feel it?")
    }

    onResponse<DescribeHeadMoreDetails> {
        furhat.ask("Where in your head do you feel it? Back, front, left or right side?")
    }
}

val PainQualityQuestion = state(parent = Options) {
    onEntry {
        furhat.ask("How do you experience the pain quality? Is it pressing, pulsing, sharp or variable?")
    }

    onResponse<DescribePainQuality> {
        val position = it.intent.position
        if (position != null) {
            goto(painQualityReceived(position))
        }
        else {
            propagate()
        }
    }
}

val PainIntensityQuestion = state(parent = Options) {
    onEntry {
        furhat.ask("How do you experience the pain intensity? Is it mild, moderate or severe?")
    }

    onResponse<DescribePainIntensity> {
        val position = it.intent.position
        if (position != null) {
            goto(painIntensityReceived(position))
        }
        else {
            propagate()
        }
    }
}

val OtherSymptomsQuestion = state(parent = Options) {
    onEntry {
        furhat.say("I will now ask you questions on other symptoms related to your head pain")
        furhat.ask("Have you experienced sensitivity to light or to sounds?")
    }

    onResponse<DescribeOtherSymptoms> {
        furhat.say("I am sorry that you are sensitive to this, it will get better")
        users.current.order.OtherLight = true
        goto(OtherEyeQuestion)
    }

    onResponse<No> {
        furhat.say("I'm glad to hear that you are not sensitive to light or sounds")
        goto(OtherEyeQuestion)
    }
}

val OtherEyeQuestion = state(parent = Options) {
    onEntry {
        furhat.ask("Do you have red or watery eye, runny nose or facial sweating on the same side as the head pain?")
    }

    onResponse<DescribeEyeSymptoms> {
        furhat.say("I hope that will improve soon! Let us move on to the next question")
        users.current.order.OtherEye = true
        goto(DurationQuestion)
    }

    onResponse<No> {
        furhat.say("Good, let us move on to the next question")
        goto(DurationQuestion)
    }
}

val DurationQuestion = state(parent = Options) {
    onEntry {
        furhat.ask("For how long is typically the duration of your head pain?")
    }

    onResponse<DescribeTime> {
        val position = it.intent.position
        if (position != null) {
            goto(durationReceived(position))
        }
        else {
            propagate()
        }
    }
}

val FrequencyQuestion = state(parent = Options) {
    onEntry {
        furhat.ask("How many days during a month do you experience your pain?")
    }

    onResponse<DescribeTime> {
        val position = it.intent.position
        if (position != null) {
            goto(frequencyReceived(position))
        }
        else {
            propagate()
        }
    }
}

/*fun OrderReceived(fruitList: FruitList) : State = state(Options) {
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
}*/

fun headDescriptionReceived(headPositions: HeadPositionList) : State = state(HeadacheQuestion) {
    onEntry {
        random(
            {furhat.say("In your ${headPositions.text}, that must be painful!")},
            {furhat.say("In your ${headPositions.text}, that is unpleasant!")},
            {furhat.say("In your ${headPositions.text}, so unfortunate!")}
        )
        headPositions.list.forEach {
            users.current.order.HeadPositions.list.add(it)
        }
        random(
            {furhat.ask("Do you feel the pain anywhere else?")},
            {furhat.ask("Is there any where else in your head or around it that you feel the pain?")},
            {furhat.ask("Can you feel the pain somewhere else?")}
        )
    }

    onReentry {
        random(
            {furhat.ask("Do you feel the pain anywhere else?")},
            {furhat.ask("Is there any where else in your head or around it that you feel the pain?")},
            {furhat.ask("Where would you say that you feel the pain?")}
        )
    }

    onResponse<No> {
        random(
            {furhat.say("Okay, then I understand that you feel some pain in your ${users.current.order.HeadPositions}.")},
            {furhat.say("So the pain is mostly in your ${users.current.order.HeadPositions}.")}
        )
        goto(confirmOrder(PainQualityQuestion, HeadacheQuestion))
    }
}

fun painQualityReceived(PainQuality: PainQualityList) : State = state(PainQualityQuestion) {
    onEntry {
        random(
            {furhat.say("It is ${PainQuality.text}, I am so sorry for you!")},
            {furhat.say("you describe it as ${PainQuality.text}, I hope it feels better soon!")}
        )
        PainQuality.list.forEach {
            users.current.order.PainQuality.list.add(it)
        }
        random(
            {furhat.ask("Can you describe your pain in any other way?")},
            {furhat.ask("Is it possible to add more to describe your pain?")}
        )
    }

    onReentry {
        random(
            {furhat.ask("Can you describe your pain in any other way?")},
            {furhat.ask("Is it possible to add more to describe your pain?")}
        )
    }

    onResponse<Yes> {
        furhat.ask("How would you describe it?")
    }

    onResponse<No> {
        random (
            {furhat.say("Okay, so the pain can be described as ${users.current.order.PainQuality}.")},
            {furhat.say("So the quality of the pain can be described as ${users.current.order.PainQuality}.")},
            {furhat.say("So you say that the pain can be described as ${users.current.order.PainQuality}.")}
        )
        goto(confirmOrder(PainIntensityQuestion, PainQualityQuestion))
    }
}

fun painIntensityReceived(PainIntensity: PainIntensityList) : State = state(PainIntensityQuestion) {
    onEntry {
        random(
            {furhat.say("It is ${PainIntensity.text}, I hope it improves!")},
            {furhat.say("you describe it as ${PainIntensity.text}, hopefully it gets better!")}
        )
        PainIntensity.list.forEach {
            users.current.order.PainIntensity.list.add(it)
        }
        random(
            {furhat.say("Okay, then I understand that the pain can be described as ${users.current.order.PainIntensity}.")},
            {furhat.say("So the intensity can then according to you be described as ${users.current.order.PainIntensity}.")},
            {furhat.say("Your pain intensity is then described as ${users.current.order.PainIntensity}.")}
        )
        goto(confirmOrder(OtherSymptomsQuestion, PainIntensityQuestion))
    }

    onResponse<Yes> {
        furhat.ask("How would you describe it?")
    }
}

fun durationReceived(Time: TimeList) : State = state(DurationQuestion) {
    onEntry {
        furhat.say("${Time.text}, okay.")

        Time.list.forEach {
            users.current.order.PainDuration.list.add(it)
        }

        goto(confirmTime(FrequencyQuestion, DurationQuestion))
    }

    onReentry {
        furhat.ask("How long is typically the duration of your head pain?")
    }
}

fun frequencyReceived(Time: TimeList) : State = state(FrequencyQuestion) {
    onEntry {
        furhat.say("${Time.text}, okay.")

        Time.list.forEach {
            users.current.order.FrequencyDuration.list.add(it)
        }

        goto(confirmTime(EvaluateAnswers, FrequencyQuestion))
    }

    onReentry {
        furhat.ask("How many days during a month do you experience your pain?")
    }
}

fun confirmTime(nextQuestion: State, redoQuestion: State) : State = state(parent=Options) {
    onEntry {
        furhat.ask("Are you satisfied with your answer?")
    }

    onResponse<Yes> {
        if(redoQuestion == DurationQuestion) {
            furhat.say("Now it is time for the final question")
        }
        if(redoQuestion == FrequencyQuestion) {
            furhat.say("Perfect, thank you for all the answers!")
        }
        goto(nextQuestion)
    }
    onResponse<AddMore> {
        furhat.say("Lets us take it again then")
        if(redoQuestion == DurationQuestion) {
            users.current.order.PainDuration = TimeList()
        }
        if(redoQuestion == FrequencyQuestion) {
            users.current.order.FrequencyDuration = TimeList()
        }
        goto(redoQuestion)
    }
}

fun confirmOrder(nextQuestion: State, redoQuestion: State) : State = state(parent=Options) {
    onEntry {
        random(
            {furhat.ask("You can add more to the description if you want, do you want to continue to the next question?")},
            {furhat.ask("It is possible to add more, do you want to move on to the next question?")},
            {furhat.ask("There is a possibility for you to add more to the description, do you want to continue to the next question?")}
        )
    }

    onResponse<Yes> {
        random(
            {furhat.say("Okay, here is the next question")},
            {furhat.say("Let us continue")},
            {furhat.say("You are doing good, let us move on")})
        goto(nextQuestion)
    }
    onResponse<AddMore> {
        furhat.say("Lets go back then!")
        if(redoQuestion == PainIntensityQuestion) {
            users.current.order.PainIntensity = PainIntensityList()
        }
        goto(redoQuestion)
    }
}

val EvaluateAnswers = state(parent = Options) {

    val tensionLocation = listOf("forehead","neck","Shoulder","back", "top")
    val tensionQuality = listOf("Pressing","tightening")
    val tensionIntensity = listOf("mild","moderate")
    // OtherLight: false, OtherEye: false

    val migraineLocation = listOf("forehead", "eyes", "neck", "Shoulder", "left side", "right side", "top", "back", "face", "front")
    val migraineQuality = listOf("pulsing","throbbing", "banging")
    val migraineIntensity= listOf("moderate","severe")
    // OtherLight: true

    val clusterLocation = listOf("eyes","left side","right side","face","front")
    val clusterQuality = listOf("Pressing", "tightening", "pulsing", "throbbing", "banging", "Variable", "sharp", "burning")
    val clusterIntensity = listOf("severe")
    // OtherEye: true

    var tensionPoints = 0
    var migrainePoints = 0
    var clusterPoints = 0

    onEntry {
        val headQuestion = users.current.order.HeadPositions.list.map { it.toString() }
        val qualityQuestion = users.current.order.HeadPositions.list.map { it.toString() }
        val intensityQuestion = users.current.order.HeadPositions.list.map { it.toString() }
        val otherLightQuestion = users.current.order.OtherLight
        val otherEyeQuestion = users.current.order.OtherEye

        val painDurationQuestion = users.current.order.PainDuration.list.map { it.toString() }
        val time = painDurationQuestion[0].split(" ")
        var durationNumber = time[0].toInt()
        val durationEntity = time[1]
        if(durationEntity == "hours"){
            durationNumber *= 60
        }
        if(durationEntity == "days"){
            durationNumber *= 60 * 24
        }

        if (durationNumber > 60*72){
            furhat.say("You have had a continuous head pain in more than 72 hours, please go directly and contact a doctor!")
        }

        val frequencyQuestion = users.current.order.FrequencyDuration.list.map { it.toString() }
        val frequencyTime = frequencyQuestion[0].split(" ")
        var frequencyNumber = frequencyTime[0].toInt()

        print("Tension points: ")
        tensionPoints = containsCheck(tensionPoints, tensionLocation, headQuestion)
        tensionPoints = containsCheck(tensionPoints, tensionQuality, qualityQuestion)
        tensionPoints = containsCheck(tensionPoints, tensionIntensity, intensityQuestion)
        tensionPoints = otherSymptomsCheck(tensionPoints, 0, otherLightQuestion, otherEyeQuestion)
        tensionPoints = durationCheck(tensionPoints, 0, durationNumber)

        print("\n Margin points: ")
        migrainePoints = containsCheck(migrainePoints, migraineLocation, headQuestion)
        migrainePoints = containsCheck(migrainePoints, migraineQuality, qualityQuestion)
        migrainePoints = containsCheck(migrainePoints, migraineIntensity, intensityQuestion)
        migrainePoints = otherSymptomsCheck(migrainePoints, 1, otherLightQuestion, otherEyeQuestion)
        migrainePoints = durationCheck(migrainePoints, 1, durationNumber)

        print("\n Cluster points: ")
        clusterPoints = containsCheck(clusterPoints, clusterLocation, headQuestion)
        clusterPoints = containsCheck(clusterPoints, clusterQuality, qualityQuestion)
        clusterPoints = containsCheck(clusterPoints, clusterIntensity, intensityQuestion)
        clusterPoints = otherSymptomsCheck(clusterPoints, 2, otherLightQuestion, otherEyeQuestion)
        clusterPoints = durationCheck(clusterPoints, 2, durationNumber)

        goto(finalPrediction(tensionPoints, migrainePoints, clusterPoints, frequencyNumber))
    }
}

fun containsCheck(points: Int, list: List<String>, question: List<String>) : Int {
    var pointsTmp = points
    if(question.any(list::contains)){
        pointsTmp += 1
        print("\n contains: $pointsTmp")
    }
    return pointsTmp
}

// type: 0 = Tension, 1 = Margin, 2 = Cluster
fun otherSymptomsCheck(points: Int, type: Int, lightQuestion: Boolean, eyeQuestion: Boolean) : Int {
    var pointsTmp = points
    if(type == 0 && !lightQuestion && !eyeQuestion){
        pointsTmp += 1
        print("\n others: $pointsTmp")
    }
    if(type == 1 && lightQuestion){
        pointsTmp += 1
        print("\n others: $pointsTmp")
    }
    if(type == 2 && eyeQuestion){
        pointsTmp += 1
        print("\n others: $pointsTmp")
    }
    return pointsTmp
}

// type: 0 = Tension, 1 = Margin, 2 = Cluster
fun durationCheck(points: Int, type: Int, duration: Int) : Int {
    var pointsTmp = points
    if(type == 0 && duration <= 30){
        pointsTmp += 1
        print("\n duration: $pointsTmp")
    }
    if(type == 1 && duration >= 60 && duration <= 60 * 72){
        pointsTmp += 1
        print("\n duration: $pointsTmp")
    }
    if(type == 2 && duration >= 15 && duration <= 180){
        pointsTmp += 1
        print("\n duration: $pointsTmp")
    }
    return pointsTmp
}

fun finalPrediction(tensionPoints: Int, migrainePoints: Int, clusterPoints: Int, frequencyNumber: Int) = state(parent = Options){
    onEntry {
        if(tensionPoints > migrainePoints && tensionPoints > clusterPoints){
            if(frequencyNumber < 15){
                furhat.say("You have most likely a episodic tension type head pain according to your answers, please contact a doctor for more information")
            }
            if(frequencyNumber >= 15){
                furhat.say("You have most likely a chronic tension type head pain according to your answers, please contact a doctor for more information")
            }
        }
        else if(migrainePoints > tensionPoints && migrainePoints > clusterPoints){
            if(frequencyNumber < 15){
                furhat.say("You have most likely a episodic migraine according to your answers, please contact a doctor for more information")
            }
            if(frequencyNumber >= 15){
                furhat.say("You have most likely a chronic migraine according to your answers, please contact a doctor for more information")
            }
        }
        else if(clusterPoints > tensionPoints && clusterPoints > migrainePoints){
            if(frequencyNumber < 15){
                furhat.say("You have most likely a episodic cluster head pain according to your answers, please contact a doctor for more information")
            }
            if(frequencyNumber >= 15){
                furhat.say("You have most likely a chronic cluster head pain according to your answers, please contact a doctor for more information")
            }
        }
        else{
            furhat.say("It was not possible to determine any head pain diagnosis, pleas contact a doctor for further investigation.")
        }

        furhat.say("Thank you for letting me help you")
        furhat.say ( "I hope that you feel better soon!" )
        goto(Idle)
    }
}
