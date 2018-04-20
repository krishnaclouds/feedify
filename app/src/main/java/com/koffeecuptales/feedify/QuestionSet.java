package com.koffeecuptales.feedify;

class QuestionSet {

    private final String[] questions = {
            "Level of Topics covered?",
            "Quality of food provided?",
            "Air Quality in the room?",
            "Level of contents in the lecture and their presentation?"
    };

    public String getQuestion(int option) {
        return questions[option];
    }

    public int getNumberofQuestions() {
        return questions.length;
    }

}
