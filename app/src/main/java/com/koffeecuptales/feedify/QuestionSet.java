package com.koffeecuptales.feedify;

/*
* SELF-TIPS
* A data class to give Question. One way to implement static Questions is to add few more Question Arrays.
* In case, we need different Options, we can make a 2-D array of options corresponding to the Question set Array and
* access them in the save way, Question set is used.
* */

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
