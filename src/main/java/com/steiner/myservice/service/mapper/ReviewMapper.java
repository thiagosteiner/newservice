package com.steiner.myservice.service.mapper;

import com.steiner.myservice.domain.*;
import com.steiner.myservice.service.dto.ReviewDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Review and its DTO ReviewDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ReviewMapper {

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "reviewVector.id", target = "reviewVectorId")
    ReviewDTO reviewToReviewDTO(Review review);

    List<ReviewDTO> reviewsToReviewDTOs(List<Review> reviews);

    @Mapping(target = "wordOccurrences", ignore = true)
    @Mapping(source = "bookId", target = "book")
    @Mapping(source = "reviewVectorId", target = "reviewVector")
    Review reviewDTOToReview(ReviewDTO reviewDTO);

    List<Review> reviewDTOsToReviews(List<ReviewDTO> reviewDTOs);

    default Book bookFromId(Long id) {
        if (id == null) {
            return null;
        }
        Book book = new Book();
        book.setId(id);
        return book;
    }

    default ReviewVector reviewVectorFromId(Long id) {
        if (id == null) {
            return null;
        }
        ReviewVector reviewVector = new ReviewVector();
        reviewVector.setId(id);
        return reviewVector;
    }
}
