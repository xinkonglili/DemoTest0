package com.shnk.service.impl;

import com.shnk.dao.impl.CompanyDaoImpl;
import com.shnk.entity.Company;
import com.shnk.entity.User;
import com.shnk.service.CompanyService;

import java.util.List;


public class CompanyServiceImpl implements CompanyService {
    private CompanyDaoImpl companyDao=new CompanyDaoImpl();
    public List<Company> getAllCompany() {
        return companyDao.getAllCompany();
    }

    @Override
    public int deleteCompanyById(String idStr,User user) {
        return companyDao.deleteCompanyById(idStr,user);
    }

    @Override
    public List<Company> getCompanyByName(String companyName) {
        return companyDao.getCompanyByName(companyName);
    }

    @Override
    public void addCompany(Company company, User user) {
        companyDao.addCompany(company,user);
    }

    @Override
    public List<Company> getCompanyById(Integer id) {
        return companyDao.getCompanyById(id);
    }

    @Override
    public int modifyCompany(Company company,User user) {
        return companyDao.modifyCompany(company,user);
    }

    @Override
    public String getCompanyNameById(int companyId) {
        return companyDao.getCompanyNameById(companyId);
    }
}
