package com.shnk.service;

import com.shnk.entity.Company;
import com.shnk.entity.User;

import java.util.List;

public interface CompanyService {
    List<Company> getAllCompany();

    int deleteCompanyById(String idStr, User user);

    List<Company> getCompanyByName(String companyName);

    void addCompany(Company company, User user);

    List<Company> getCompanyById(Integer id);

    int modifyCompany(Company company, User user);

    String getCompanyNameById(int companyId);
}
