package com.capgemini.ocean.institute.training;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import com.capgemini.ocean.institute.training.entity.Feedback;

import com.capgemini.ocean.institute.training.repo.FeedbackRepository;

import com.capgemini.ocean.institute.training.service.FeedbackService;

@SpringBootTest(classes = AdvancedTouristGuideFeedback1Application.class)
@AutoConfigureMockMvc
public class AdvancedTouristGuideFeedback1ApplicationTests {

	
	


	@Autowired
	private FeedbackService feedbackService;

	@MockBean
	private FeedbackRepository feedbackRepository;

	@Test
	public void testGetAllFeedbacks() {
		// Arrange
		Feedback feedback1 = new Feedback();
		Feedback feedback2 = new Feedback();
		List<Feedback> feedbackList = Arrays.asList(feedback1, feedback2);

		when(feedbackRepository.findAll()).thenReturn(feedbackList);

		// Act
		List<Feedback> result = feedbackService.getAllFeedbacks();

		// Assert
		assertEquals(feedbackList.size(), result.size());
		assertEquals(feedback1, result.get(0));
		assertEquals(feedback2, result.get(1));
		verify(feedbackRepository, times(1)).findAll();
	}

	@Test
	public void testSaveFeedback() {
		// Arrange
		Feedback feedback = new Feedback();

		when(feedbackRepository.save(feedback)).thenReturn(feedback);

		// Act
		Feedback savedFeedback = feedbackService.saveFeedback(feedback);

		// Assert
		assertEquals(feedback, savedFeedback);
		verify(feedbackRepository, times(1)).save(feedback);
	}

	

	
	// Additional test cases for FeedbackService

	@Test
	public void testGetFeedbackById() {
		// Arrange
		long feedbackId = 1L;
		Feedback feedback = new Feedback();
		when(feedbackRepository.findById(feedbackId)).thenReturn(Optional.of(feedback));

		// Act
		Feedback result = feedbackService.getFeedbackById(feedbackId);

		// Assert
		assertEquals(feedback, result);
		verify(feedbackRepository, times(1)).findById(feedbackId);
	}

	@Test
	public void testDeleteFeedback() {
		// Arrange
		long feedbackId = 1L;

		// Act
		feedbackService.deleteFeedback(feedbackId);

		// Assert
		verify(feedbackRepository, times(1)).deleteById(feedbackId);
	}

	// Additional test cases for CustomerUserService

	

	// Additional test cases for FeedbackService

	@Test
	public void testGetAllFeedbacksEmptyList() {
		// Arrange
		when(feedbackRepository.findAll()).thenReturn(Collections.emptyList());

		// Act
		List<Feedback> result = feedbackService.getAllFeedbacks();

		// Assert
		assertTrue(result.isEmpty());
		verify(feedbackRepository, times(1)).findAll();
	}

	

	// Additional test cases for FeedbackService

	@Test
	public void testGetFeedbackByIdWithNonexistentId() {
		// Arrange
		long nonExistingFeedbackId = 999L;
		when(feedbackRepository.findById(nonExistingFeedbackId)).thenReturn(Optional.empty());

		// Act
		Optional<Feedback> feedback = Optional.ofNullable(feedbackService.getFeedbackById(nonExistingFeedbackId));

		// Assert
		assertFalse(feedback.isPresent());
		verify(feedbackRepository, times(1)).findById(nonExistingFeedbackId);
	}

	@Test
	public void testGetFeedbackByIdNotFound() {
		// Arrange
		long nonExistingFeedbackId = 999L;
		when(feedbackRepository.findById(nonExistingFeedbackId)).thenReturn(Optional.empty());

		// Act
		Feedback result = feedbackService.getFeedbackById(nonExistingFeedbackId);

		// Assert
		assertNull(result);
		verify(feedbackRepository, times(1)).findById(nonExistingFeedbackId);
	}

	

	@Test
	public void testUpdateFeedbackNonexistent() {
		// Arrange
		long feedbackId = 999L;
		Feedback updatedFeedback = new Feedback();

		when(feedbackRepository.findById(feedbackId)).thenReturn(Optional.empty());

		// Act
		Feedback result = feedbackService.updateFeedback(feedbackId, updatedFeedback);

		// Assert
		assertNull(result);
		verify(feedbackRepository, times(1)).findById(feedbackId);
		verify(feedbackRepository, never()).save(any(Feedback.class));
	}

	

	@Test
	public void testDeleteFeedbackNonexistent() {
		// Arrange
		long feedbackId = 999L;

		// Act
		feedbackService.deleteFeedback(feedbackId);

		// Assert
		verify(feedbackRepository, times(1)).deleteById(feedbackId);
		// Ensure that deleteById is called even if the feedback doesn't exist (no
		// exception)
	}

	

	

	@Test
	public void testSaveFeedbackNullFeedback() {
		// Act
		Feedback result = feedbackService.saveFeedback(null);

		// Assert
		assertNull(result);
		verify(feedbackRepository, never()).save(any(Feedback.class));
	}

	@Test
	public void testUpdateFeedbackNullFeedback() {
		// Act
		Feedback result = feedbackService.updateFeedback(1L, null);

		// Assert
		assertNull(result);
		verify(feedbackRepository, never()).save(any(Feedback.class));
	}

	

}
