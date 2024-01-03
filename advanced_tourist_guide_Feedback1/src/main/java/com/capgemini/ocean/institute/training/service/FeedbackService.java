package com.capgemini.ocean.institute.training.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.ocean.institute.training.entity.Feedback;
import com.capgemini.ocean.institute.training.repo.FeedbackRepository;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    public Feedback getFeedbackById(long feedbackId) {
        return feedbackRepository.findById(feedbackId).orElse(null);
    }

    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public Feedback updateFeedback(long feedbackId, Feedback updatedFeedback) {
        Optional<Feedback> existingFeedbackOptional = feedbackRepository.findById(feedbackId);

        if (existingFeedbackOptional.isPresent()) {
            Feedback existingFeedback = existingFeedbackOptional.get();
            existingFeedback.setComment(updatedFeedback.getComment());
            // Add more fields to update as needed
            return feedbackRepository.save(existingFeedback);
        } else {
            // Handle the case when the feedback with the given ID is not found
            return null;
        }
    }

    public void deleteFeedback(long feedbackId) {
        feedbackRepository.deleteById(feedbackId);
    }
}
