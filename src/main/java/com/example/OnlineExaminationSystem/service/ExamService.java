package com.example.OnlineExaminationSystem.service;

import com.example.OnlineExaminationSystem.dto.ExamCreateRequest;
import com.example.OnlineExaminationSystem.dto.ExamSectionRequest;
import com.example.OnlineExaminationSystem.dto.QuestionOptionRequest;
import com.example.OnlineExaminationSystem.dto.QuestionRequest;
import com.example.OnlineExaminationSystem.entity.Exam;
import com.example.OnlineExaminationSystem.entity.ExamSection;
import com.example.OnlineExaminationSystem.entity.Question;
import com.example.OnlineExaminationSystem.entity.QuestionOption;
import com.example.OnlineExaminationSystem.enums.ExamStatus;
import com.example.OnlineExaminationSystem.enums.QuestionType;
import com.example.OnlineExaminationSystem.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    // create exam
    public Exam createExam(ExamCreateRequest request) {
        Exam exam = new Exam();
        exam.setExamName(request.getExamName());
        exam.setStartTime(request.getStartTime());
        exam.setEndTime(request.getEndTime());
        exam.setDuration(request.getDuration());
        exam.setStatus(ExamStatus.UPCOMING);

        List<ExamSection> sections = new ArrayList<>();

        for(ExamSectionRequest secReq : request.getSections()) {

            ExamSection section = new ExamSection();
            section.setSectionName(secReq.getSectionName());
            section.setDuration(secReq.getDuration());
            section.setCompulsory(secReq.isCompulsory());
            section.setType(secReq.getType());
            section.setExam(exam);

            List<Question> questions = new ArrayList<>();

            for(QuestionRequest qReq : secReq.getQuestions()) {

                Question question = new Question();
                question.setQuestionText(qReq.getQuestionText());
                question.setType(qReq.getType());
                question.setMarks(qReq.getMarks());
                question.setSection(section);

                // mcq validation
                if(qReq.getType() == QuestionType.MCQ) {
                    if(qReq.getOptions() == null || qReq.getOptions().isEmpty()) {
                        throw new RuntimeException("MCQ must have options");
                    }

                    List<QuestionOption> options = new ArrayList<>();
                    for(QuestionOptionRequest optionReq : qReq.getOptions()) {
                        QuestionOption option = new QuestionOption();
                        option.setOptionText(optionReq.getOptionText());
                        option.setCorrect(optionReq.isCorrect());
                        option.setQuestion(question);
                        options.add(option);
                    }
                    question.setOptions(options);
                }

                questions.add(question);
            }

            section.setQuestions(questions);
            sections.add(section);
        }

        exam.setSections(sections);
        return examRepository.save(exam);
    }
}
