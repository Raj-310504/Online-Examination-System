package com.example.OnlineExaminationSystem.service;

import com.example.OnlineExaminationSystem.dto.ExamResponseDTO;
import com.example.OnlineExaminationSystem.dto.ExamSectionResponseDTO;
import com.example.OnlineExaminationSystem.dto.StartExamRequest;
import com.example.OnlineExaminationSystem.dto.StartExamResponse;
import com.example.OnlineExaminationSystem.dto.SubmitAnswerRequest;
import com.example.OnlineExaminationSystem.dto.StudentExamQuestionsResponse;
import com.example.OnlineExaminationSystem.dto.StudentQuestionDTO;
import com.example.OnlineExaminationSystem.dto.StudentQuestionOptionDTO;
import com.example.OnlineExaminationSystem.dto.StudentSectionQuestionsDTO;
import com.example.OnlineExaminationSystem.entity.Exam;
import com.example.OnlineExaminationSystem.entity.Question;
import com.example.OnlineExaminationSystem.entity.QuestionOption;
import com.example.OnlineExaminationSystem.entity.Student;
import com.example.OnlineExaminationSystem.entity.StudentAnswer;
import com.example.OnlineExaminationSystem.entity.StudentExam;
import com.example.OnlineExaminationSystem.enums.ExamAttemptStatus;
import com.example.OnlineExaminationSystem.enums.ExamStatus;
import com.example.OnlineExaminationSystem.enums.QuestionType;
import com.example.OnlineExaminationSystem.repository.ExamRepository;
import com.example.OnlineExaminationSystem.repository.QuestionOptionRepository;
import com.example.OnlineExaminationSystem.repository.QuestionRepository;
import com.example.OnlineExaminationSystem.repository.StudentAnswerRepository;
import com.example.OnlineExaminationSystem.repository.StudentExamRepository;
import com.example.OnlineExaminationSystem.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentExamService {

    @Autowired
    private StudentExamRepository studentExamRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private StudentAnswerRepository studentAnswerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionOptionRepository questionOptionRepository;

    @Autowired
    private StudentRepository studentRepository;

    // get all active exams
    public List<ExamResponseDTO> getActiveExams() {
        LocalDateTime now = LocalDateTime.now();

        List<Exam> exams = examRepository
                .findByStatusAndStartTimeBeforeAndEndTimeAfter(
                        ExamStatus.ONGOING,
                        now,
                        now
                );

        return exams.stream()
                .map(this::mapToExamResponse)
                .toList();
    }

    // mapper
    private ExamResponseDTO mapToExamResponse(Exam exam) {

        ExamResponseDTO dto = new ExamResponseDTO();
        dto.setExamId(exam.getId());
        dto.setExamName(exam.getExamName());
        dto.setStartTime(exam.getStartTime());
        dto.setEndTime(exam.getEndTime());
        dto.setTotalDuration(exam.getDuration());
        dto.setStatus(exam.getStatus());

        List<ExamSectionResponseDTO> sectionDTOs =
                exam.getSections() == null ? List.of() :
                        exam.getSections().stream()
                                .map(section -> {
                                    ExamSectionResponseDTO sDto =
                                            new ExamSectionResponseDTO();
                                    sDto.setSectionId(section.getId());
                                    sDto.setSectionName(section.getSectionName());
                                    sDto.setType(section.getType());
                                    sDto.setDuration(section.getDuration());
                                    sDto.setCompulsory(section.isCompulsory());
                                    return sDto;
                                })
                                .toList();

        dto.setSections(sectionDTOs);
        return dto;
    }

    @Transactional
    public StartExamResponse startExam(StartExamRequest request) {
        Student student = studentRepository.findByStudentId(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        if (exam.getStatus() != ExamStatus.ONGOING) {
            throw new RuntimeException("Exam is not ongoing");
        }

        StudentExam studentExam = studentExamRepository
                .findByStudentIdAndExamId(student.getId(), exam.getId())
                .orElseGet(() -> {
                    StudentExam attempt = new StudentExam();
                    attempt.setStudent(student);
                    attempt.setExam(exam);
                    attempt.setStartedAt(LocalDateTime.now());
                    attempt.setEndedAt(null);
                    attempt.setRemainingTime(exam.getDuration());
                    attempt.setStatus(ExamAttemptStatus.IN_PROGRESS);
                    return attempt;
                });

        if (studentExam.getStatus() == ExamAttemptStatus.SUBMITTED) {
            throw new RuntimeException("Exam already submitted by this student");
        }

        StudentExam saved = studentExamRepository.save(studentExam);
        return new StartExamResponse(
                "Exam attempt ready",
                saved.getId(),
                exam.getId(),
                student.getStudentId()
        );
    }

    public StudentExamQuestionsResponse getExamQuestions(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        List<StudentSectionQuestionsDTO> sectionDTOs = exam.getSections() == null
                ? List.of()
                : exam.getSections().stream()
                .map(section -> {
                    List<StudentQuestionDTO> questionDTOs = section.getQuestions() == null
                            ? List.of()
                            : section.getQuestions().stream()
                            .map(question -> {
                                List<StudentQuestionOptionDTO> optionDTOs = question.getType() == QuestionType.MCQ
                                        ? (question.getOptions() == null
                                        ? List.of()
                                        : question.getOptions().stream()
                                        .map(option -> new StudentQuestionOptionDTO(
                                                option.getId(),
                                                option.getOptionText()
                                        ))
                                        .toList())
                                        : List.of();

                                return new StudentQuestionDTO(
                                        question.getId(),
                                        question.getQuestionText(),
                                        question.getType(),
                                        question.getMarks(),
                                        optionDTOs
                                );
                            })
                            .toList();

                    return new StudentSectionQuestionsDTO(
                            section.getId(),
                            section.getSectionName(),
                            questionDTOs
                    );
                })
                .toList();

        return new StudentExamQuestionsResponse(
                exam.getId(),
                exam.getExamName(),
                sectionDTOs
        );
    }

    @Transactional
    public StudentAnswer submitAnswer(SubmitAnswerRequest request) {
        StudentExam studentExam = studentExamRepository.findById(request.getStudentExamId())
                .orElseThrow(() -> new RuntimeException("Student exam attempt not found"));

        if (studentExam.getStatus() != ExamAttemptStatus.IN_PROGRESS) {
            throw new RuntimeException("Exam attempt is already submitted");
        }

        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found"));

        if (!question.getSection().getExam().getId().equals(studentExam.getExam().getId())) {
            throw new RuntimeException("Question does not belong to this exam attempt");
        }

        StudentAnswer answer = studentAnswerRepository
                .findByStudentExamIdAndQuestionId(studentExam.getId(), question.getId())
                .orElseGet(() -> {
                    StudentAnswer newAnswer = new StudentAnswer();
                    newAnswer.setStudentExam(studentExam);
                    newAnswer.setQuestion(question);
                    return newAnswer;
                });

        if (question.getType() == QuestionType.MCQ) {
            if (request.getSelectedOptionId() == null) {
                throw new RuntimeException("selectedOptionId is required for MCQ");
            }

            QuestionOption selectedOption = questionOptionRepository.findById(request.getSelectedOptionId())
                    .orElseThrow(() -> new RuntimeException("Selected option not found"));

            if (!selectedOption.getQuestion().getId().equals(question.getId())) {
                throw new RuntimeException("Selected option does not belong to this question");
            }

            answer.setSelectedOption(selectedOption);
            answer.setAnswerText(null);
        } else {
            if (request.getAnswerText() == null || request.getAnswerText().isBlank()) {
                throw new RuntimeException("answerText is required for descriptive questions");
            }

            answer.setAnswerText(request.getAnswerText().trim());
            answer.setSelectedOption(null);
        }

        StudentAnswer savedAnswer = studentAnswerRepository.save(answer);

        int totalQuestions = getTotalQuestionCount(studentExam.getExam());
        long answeredQuestions = studentAnswerRepository
                .countDistinctQuestionsByStudentExamId(studentExam.getId());

        if (totalQuestions > 0 && answeredQuestions >= totalQuestions) {
            studentExam.setStatus(ExamAttemptStatus.SUBMITTED);
            studentExam.setEndedAt(LocalDateTime.now());
            studentExam.setRemainingTime(0);
            studentExamRepository.save(studentExam);
        }

        return savedAnswer;
    }

    private int getTotalQuestionCount(Exam exam) {
        if (exam.getSections() == null) {
            return 0;
        }

        return exam.getSections().stream()
                .mapToInt(section -> section.getQuestions() == null ? 0 : section.getQuestions().size())
                .sum();
    }
}
