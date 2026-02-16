package com.example.OnlineExaminationSystem.service;

import com.example.OnlineExaminationSystem.dto.*;
import com.example.OnlineExaminationSystem.entity.Exam;
import com.example.OnlineExaminationSystem.entity.ExamSection;
import com.example.OnlineExaminationSystem.entity.Question;
import com.example.OnlineExaminationSystem.entity.QuestionOption;
import com.example.OnlineExaminationSystem.enums.ExamStatus;
import com.example.OnlineExaminationSystem.enums.QuestionType;
import com.example.OnlineExaminationSystem.exception.BadRequestException;
import com.example.OnlineExaminationSystem.repository.ExamRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExamService {

    private final ExamRepository examRepository;

    public ExamService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

//    @Autowired
//    private RestTemplate restTemplate;
//
//    public UserResponse getUser() {
//
//        String url = "https://jsonplaceholder.typicode.com/users/1";
//        return restTemplate.getForObject(url, UserResponse.class);
//    }

    // create exam
    public Exam createExam(ExamCreateRequest request) {
        validateExamRequest(request);

        Exam exam = new Exam();
        exam.setExamName(request.getExamName());
        exam.setStartTime(request.getStartTime());
        exam.setEndTime(request.getEndTime());
        exam.setDuration(request.getDuration());
        exam.setStatus(ExamStatus.UPCOMING);

        List<ExamSection> sections = new ArrayList<>();

        for (ExamSectionRequest secReq : request.getSections()) {

            ExamSection section = new ExamSection();
            section.setSectionName(secReq.getSectionName());
            section.setDuration(secReq.getDuration());
            section.setCompulsory(secReq.isCompulsory());
            section.setType(secReq.getType());
            section.setExam(exam);

            List<Question> questions = new ArrayList<>();

            for (QuestionRequest qReq : secReq.getQuestions()) {

                Question question = new Question();
                question.setQuestionText(qReq.getQuestionText());
                question.setType(qReq.getType());
                question.setMarks(qReq.getMarks());
                question.setSection(section);

                // mcq validation
                if (qReq.getType() == QuestionType.MCQ) {
                    if (qReq.getOptions() == null || qReq.getOptions().isEmpty()) {
                        throw new BadRequestException("MCQ must have options");
                    }

                    long correctCount = qReq.getOptions().stream().filter(QuestionOptionRequest::isCorrect).count();
                    if (correctCount != 1) {
                        throw new BadRequestException("MCQ must have exactly one correct option");
                    }

                    List<QuestionOption> options = new ArrayList<>();
                    for (QuestionOptionRequest optionReq : qReq.getOptions()) {
                        QuestionOption option = new QuestionOption();
                        option.setOptionText(optionReq.getOptionText());
                        option.setCorrect(optionReq.isCorrect());
                        option.setQuestion(question);
                        options.add(option);
                    }
                    question.setOptions(options);
                } else if (qReq.getOptions() != null && !qReq.getOptions().isEmpty()) {
                    throw new BadRequestException("Only MCQ questions can contain options");
                }

                questions.add(question);
            }

            section.setQuestions(questions);
            sections.add(section);
        }

        exam.setSections(sections);
        return examRepository.save(syncStatus(exam));
    }

    public int refreshExamStatuses() {
        LocalDateTime now = LocalDateTime.now();
        List<Exam> exams = examRepository.findAll();
        int updated = 0;

        for (Exam exam : exams) {
            if (exam.getStatus() == ExamStatus.CANCELLED) {
                continue;
            }

            ExamStatus newStatus = computeStatus(now, exam);

            if (newStatus != exam.getStatus()) {
                exam.setStatus(newStatus);
                updated++;
            }
        }

        if (updated > 0) {
            examRepository.saveAll(exams);
        }

        return updated;
    }

    public Exam syncAndSaveStatus(Exam exam) {
        ExamStatus before = exam.getStatus();
        Exam synced = syncStatus(exam);
        if (before != synced.getStatus()) {
            return examRepository.save(synced);
        }
        return synced;
    }

    private void validateExamRequest(ExamCreateRequest request) {
        if (request.getStartTime() == null || request.getEndTime() == null) {
            throw new BadRequestException("Exam start time and end time are required");
        }
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new BadRequestException("Exam end time must be after start time");
        }
        if (request.getSections() == null || request.getSections().isEmpty()) {
            throw new BadRequestException("Exam must contain at least one section");
        }
        for (ExamSectionRequest section : request.getSections()) {
            if (section.getQuestions() == null || section.getQuestions().isEmpty()) {
                throw new BadRequestException("Section must contain at least one question");
            }
        }
    }

    private Exam syncStatus(Exam exam) {
        if (exam.getStatus() == ExamStatus.CANCELLED) {
            return exam;
        }
        exam.setStatus(computeStatus(LocalDateTime.now(), exam));
        return exam;
    }

    private ExamStatus computeStatus(LocalDateTime now, Exam exam) {
        if (now.isBefore(exam.getStartTime())) {
            return ExamStatus.UPCOMING;
        }
        if (!now.isBefore(exam.getEndTime())) {
            return ExamStatus.COMPLETED;
        }
        return ExamStatus.ONGOING;
    }
}
