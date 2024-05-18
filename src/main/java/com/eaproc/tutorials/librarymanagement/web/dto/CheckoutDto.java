package com.eaproc.tutorials.librarymanagement.web.dto;

import com.eaproc.tutorials.librarymanagement.domain.model.BookEntity;
import com.eaproc.tutorials.librarymanagement.domain.model.UserEntity;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CheckoutDto {
    private Long checkoutId;
    private Long userId;
    private Long bookId;
    private String checkoutPersonName;
    private String checkoutPersonEmailAddress;
    private String bookTitle;
    private String bookAuthor;
    private String bookIsbn;

    @Setter
    private LocalDateTime checkoutDateTimeUtc;
    @Setter
    private LocalDateTime returnDateTimeUtc;
    @Setter
    private LocalDateTime createdAt;
    @Setter
    private LocalDateTime updatedAt;

    public void setId(Long checkoutId) {
        this.checkoutId = checkoutId;
    }

    public void setBookEntity(final BookEntity bookEntity) {
        this.bookId = bookEntity.getId();
        this.bookTitle = bookEntity.getTitle();
        this.bookAuthor = bookEntity.getAuthor();
        this.bookIsbn = bookEntity.getIsbn();
    }

    public void setUserEntity(final UserEntity userEntity) {
        this.userId = userEntity.getId();
        this.checkoutPersonName = userEntity.getName();
        this.checkoutPersonEmailAddress = userEntity.getEmail();
    }
}
