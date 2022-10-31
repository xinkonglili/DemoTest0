package com.shnk.dao;

import com.shnk.entity.Company;
import com.shnk.entity.User;

import java.util.List;

public interface CompanyDao {
    List<Company> getAllCompany();

    int deleteCompanyById(String idStr, User user);

    void addCompany(Company company, User user);

    List<Company> getCompanyById(Integer id);

    int modifyCompany(Company company, User user);

    String getCompanyNameById(int companyId);
}
