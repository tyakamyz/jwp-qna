package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import qna.NotFoundException;
import qna.UnAuthorizedException;

@Entity
public class Answer extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Lob
	private String contents;
	@Column(nullable = false)
	private boolean deleted = false;
	private Long questionId;
	private Long writerId;

	public Answer(User writer, Question question, String contents) {
		this(null, writer, question, contents);
	}

	public Answer(Long id, User writer, Question question, String contents) {
		LocalDateTime createdAt = LocalDateTime.now();
		this.id = id;

		if (Objects.isNull(writer)) {
			throw new UnAuthorizedException();
		}

		if (Objects.isNull(question)) {
			throw new NotFoundException();
		}

		this.writerId = writer.getId();
		this.questionId = question.getId();
		this.contents = contents;
		this.createdAt = createdAt;
		this.updatedAt = createdAt;
	}

	protected Answer() {
	}

	public boolean isOwner(User writer) {
		return this.writerId.equals(writer.getId());
	}

	public void toQuestion(Question question) {
		this.questionId = question.getId();
	}

	public Long getId() {
		return id;
	}

	public Long getWriterId() {
		return writerId;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "Answer{" +
			"id=" + id +
			", writerId=" + writerId +
			", questionId=" + questionId +
			", contents='" + contents + '\'' +
			", deleted=" + deleted +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Answer answer = (Answer)o;
		return deleted == answer.deleted && Objects.equals(id, answer.id) && Objects.equals(contents,
			answer.contents) && Objects.equals(questionId, answer.questionId) && Objects.equals(
			writerId, answer.writerId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, contents, deleted, questionId, writerId);
	}
}
