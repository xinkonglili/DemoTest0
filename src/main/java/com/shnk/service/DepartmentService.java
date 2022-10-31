package com.shnk.service;

import com.shnk.entity.Department;
import com.shnk.entity.DepartmentPOVO;
import com.shnk.entity.StaffCount;
import com.shnk.entity.User;

import java.util.List;

public interface DepartmentService {
    List<Department> getDepartmentByCompanyId(Integer companyId);


    List<DepartmentPOVO> getDepartment(Integer companyId);

    Boolean addDepartment(Department department, User user);

    int deleteDepartment(String idStr, User user);

    int modifyDepartment(Department department, User user);

    List<StaffCount> getStaffCount(Integer companyId, Integer departmentId);

    String getDepartmentNameById(int departmentId);
}
