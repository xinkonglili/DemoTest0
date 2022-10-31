package com.shnk.service.impl;

import com.shnk.dao.impl.DepartmentDaoImpl;
import com.shnk.entity.Department;
import com.shnk.entity.DepartmentPOVO;
import com.shnk.entity.StaffCount;
import com.shnk.entity.User;
import com.shnk.service.DepartmentService;

import java.util.List;

public class DepartmentServiceImpl implements DepartmentService {
    DepartmentDaoImpl departmentDao=new DepartmentDaoImpl();
    @Override
    public List<Department> getDepartmentByCompanyId(Integer companyId) {
        return departmentDao.getDepartmentByCompanyId(companyId);
    }

    @Override
    public List<DepartmentPOVO> getDepartment(Integer companyId) {
        return departmentDao.getDepartment(companyId);
    }

    @Override
    public Boolean addDepartment(Department department, User user) {
        return departmentDao.addDepartment(department,user);
    }

    @Override
    public int deleteDepartment(String idStr,User user) {
        return departmentDao.deleteDepartment(idStr,user);
    }

    @Override
    public int modifyDepartment(Department department,User user) {
        return departmentDao.modifyDepartment(department,user);
    }

    @Override
    public List<StaffCount> getStaffCount(Integer companyId, Integer departmentId) {
        return departmentDao.getStaffCount(companyId,departmentId);
    }

    @Override
    public String getDepartmentNameById(int departmentId) {
        return departmentDao.getDepartmentNameById(departmentId);
    }


}
