package com.koffeecuptales.feedify;

class QuestionSet {

    private final String[] questions = {
            "Presentation skills of the speaker",
            "Question session",
            "Faculty",
            "Content of the course",
            "Relevance of the Topic"
    };

    public String getQuestion(int option) {
        return questions[option];
    }

    public int getNumberofQuestions() {
        return questions.length;
    }

}
