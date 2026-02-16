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
import com.example.OnlineExaminationSystem.exception.BadRequestException;
import com.example.OnlineExaminationSystem.exception.ConflictException;
import com.example.OnlineExaminationSystem.exception.NotFoundException;
import com.example.OnlineExaminationSystem.repository.ExamRepository;
import com.example.OnlineExaminationSystem.repository.QuestionOptionRepository;
import com.example.OnlineExaminationSystem.repository.QuestionRepository;
import com.example.OnlineExaminationSystem.repository.StudentAnswerRepository;
import com.example.OnlineExaminationSystem.repository.StudentExamRepository;
import com.example.OnlineExaminationSystem.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentExamService {

    private final StudentExamRepository studentExamRepository;
    private final ExamRepository examRepository;
    private final StudentAnswerRepository studentAnswerRepository;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final StudentRepository studentRepository;
    private final ExamService examService;

    public StudentExamService(StudentExamRepository studentExamRepository,
                              ExamRepository examRepository,
                              StudentAnswerRepository studentAnswerRepository,
                              QuestionRepository questionRepository,
                              QuestionOptionRepository questionOptionRepository,
                              StudentRepository studentRepository,
                              ExamService examService) {
        this.studentExamRepository = studentExamRepository;
        this.examRepository = examRepository;
        this.studentAnswerRepository = studentAnswerRepository;
        this.questionRepository = questionRepository;
        this.questionOptionRepository = questionOptionRepository;
        this.studentRepository = studentRepository;
        this.examService = examService;
    }

    // get all active exams
    public List<ExamResponseDTO> getActiveExams() {
        LocalDateTime now = LocalDateTime.now();

        List<Exam> exams = examRepository
                .findByStartTimeBeforeAndEndTimeAfter(now, now);

        return exams.stream()
                .map(examService::syncAndSaveStatus)
                .filter(exam -> exam.getStatus() == ExamStatus.ONGOING)
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
                .orElseThrow(() -> new NotFoundException("Student not found"));

        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new NotFoundException("Exam not found"));
        Exam syncedExam = examService.syncAndSaveStatus(exam);

        if (syncedExam.getStatus() != ExamStatus.ONGOING) {
            throw new ConflictException("Exam is not ongoing");
        }

        StudentExam studentExam = studentExamRepository
                .findByStudentIdAndExamId(student.getId(), syncedExam.getId())
                .orElseGet(() -> {
                    StudentExam attempt = new StudentExam();
                    attempt.setStudent(student);
                    attempt.setExam(syncedExam);
                    attempt.setStartedAt(LocalDateTime.now());
                    attempt.setEndedAt(null);
                    attempt.setRemainingTime(syncedExam.getDuration());
                    attempt.setStatus(ExamAttemptStatus.IN_PROGRESS);
                    return attempt;
                });

        if (studentExam.getStatus() == ExamAttemptStatus.SUBMITTED) {
            throw new ConflictException("Exam already submitted by this student");
        }

        StudentExam saved = studentExamRepository.save(studentExam);
        return new StartExamResponse(
                "Exam attempt ready",
                saved.getId(),
                syncedExam.getId(),
                student.getStudentId()
        );
    }

    public StudentExamQuestionsResponse getExamQuestions(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new NotFoundException("Exam not found"));

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
                .orElseThrow(() -> new NotFoundException("Student exam attempt not found"));

        if (studentExam.getStatus() != ExamAttemptStatus.IN_PROGRESS) {
            throw new ConflictException("Exam attempt is already submitted");
        }

        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new NotFoundException("Question not found"));

        if (!question.getSection().getExam().getId().equals(studentExam.getExam().getId())) {
            throw new BadRequestException("Question does not belong to this exam attempt");
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
                throw new BadRequestException("selectedOptionId is required for MCQ");
            }

            QuestionOption selectedOption = questionOptionRepository.findById(request.getSelectedOptionId())
                    .orElseThrow(() -> new NotFoundException("Selected option not found"));

            if (!selectedOption.getQuestion().getId().equals(question.getId())) {
                throw new BadRequestException("Selected option does not belong to this question");
            }

            answer.setSelectedOption(selectedOption);
            answer.setAnswerText(null);
        } else {
            if (request.getAnswerText() == null || request.getAnswerText().isBlank()) {
                throw new BadRequestException("answerText is required for descriptive questions");
            }

            answer.setAnswerText(request.getAnswerText().trim());
            answer.setSelectedOption(null);
        }

        StudentAnswer savedAnswer = studentAnswerRepository.save(answer);

        Set<Long> requiredQuestionIds = getRequiredQuestionIds(studentExam.getExam());
        if (!requiredQuestionIds.isEmpty()) {
            long answeredQuestions = studentAnswerRepository
                    .countDistinctQuestionsByStudentExamIdAndQuestionIdIn(studentExam.getId(), requiredQuestionIds);

            if (answeredQuestions >= requiredQuestionIds.size()) {
                studentExam.setStatus(ExamAttemptStatus.SUBMITTED);
                studentExam.setEndedAt(LocalDateTime.now());
                studentExam.setRemainingTime(0);
                studentExamRepository.save(studentExam);
            }
        }

        return savedAnswer;
    }

    private Set<Long> getRequiredQuestionIds(Exam exam) {
        if (exam.getSections() == null) {
            return Set.of();
        }

        boolean hasCompulsorySection = exam.getSections().stream().anyMatch(section -> section.isCompulsory());

        return exam.getSections().stream()
                .filter(section -> !hasCompulsorySection || section.isCompulsory())
                .flatMap(section -> section.getQuestions() == null ? List.<Question>of().stream() : section.getQuestions().stream())
                .map(Question::getId)
                .collect(Collectors.toSet());
    }
}
