package com.example.securerepo.repository;

import com.example.securerepo.dao.PasswordCheckerDAO;
import com.example.securerepo.model.PasswordChecker;

import io.reactivex.Single;

public class PasswordCheckerSource {

    private PasswordCheckerDAO passwordCheckerDAO;

    public PasswordCheckerSource(PasswordCheckerDAO passwordCheckerDAO) {
        this.passwordCheckerDAO = passwordCheckerDAO;
    }

    public void updatePasswordChecker(PasswordChecker checker){
        passwordCheckerDAO.updatePasswordChecker(checker);
    }

    public void insertPasswordChecker (PasswordChecker checker){
        passwordCheckerDAO.insertPasswordChecker(checker);
    }

    public Single<PasswordChecker> getPasswordChecker (int id){
       return passwordCheckerDAO.getPasswordChecker(id);
    }
}
