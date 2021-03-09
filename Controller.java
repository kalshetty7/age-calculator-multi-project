package com.file.manager.ctrls;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.file.manager.entities.Company;
import com.file.manager.entities.Employee;
import com.file.manager.entities.OtherDetails;
import com.file.manager.entities.Project;
import com.file.manager.entities.Skill;



/**
 *
 * @author anupkalshetty
 */

@org.springframework.stereotype.Controller
public class Controller {

	@InitBinder
    public void customizeBinding (WebDataBinder binder) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy");
        dateFormatter.setLenient(false);
        binder.registerCustomEditor(Date.class,new CustomDateEditor(dateFormatter, true));
    }
	
	
@RequestMapping("/")
public ModelAndView home(ModelMap m,@ModelAttribute("employee") Employee e){
    m.put("employee", (e==null)?new Employee():e);
    List<Employee> employees = new ArrayList<Employee>();
    m.put("employees", employees);
    ModelAndView mv = new ModelAndView("index",m);
    return mv;
} 

@RequestMapping("/delete")
public String delete(@RequestParam("ids") List<Long> ids){
    if(!CollectionUtils.isEmpty(ids)) {
    	//genericService.deleteById(ids);
    }
    return "redirect:/";
} 


@RequestMapping("/deleteField")
public String deleteField(@ModelAttribute("employee") Employee e,@RequestParam("ids") List<Integer> indexes,@RequestParam("field") String field,RedirectAttributes ra){
    if(!CollectionUtils.isEmpty(indexes) && field!=null) {
    	if(field.equals("companies")) {
    		List<Company> companies = e.getCompanies();
    		Utils.removeElements(companies, indexes);
    		e.setCompanies(companies);
    	}
    }
    ra.addFlashAttribute("employee", e);
    return "redirect:/";
} 

@RequestMapping("/loadDefaultData")
public String loadDefaultData(RedirectAttributes ra){
	Employee e = Utils.getDefaultData();
	//genericService.save(e);
	ra.addFlashAttribute("employee", e);
    return "redirect:/";
} 


@RequestMapping("/backup")
public String backup(){
	PersisterFactory<Employee> p = new PersisterFactory<Employee>();
	//for(Employee e : genericService.getAllObjects())
	//	p.backup(e);
    return "redirect:/";
}

@RequestMapping("/cv/{eid}")
public String cv(@PathVariable Long eid,ModelMap m){
    Employee e = (eid==null)?new Employee():new Employee();
    OtherDetails o = new OtherDetails(e);
    m.put("e", e);
    m.put("o", o);
    return "cv";
} 

@RequestMapping("/restore")
public String restore(){
	PersisterFactory<Employee> p = new PersisterFactory<Employee>();
	List<Employee> empList = p.restore();
	if(!CollectionUtils.isEmpty(empList)) {
		for(Employee e : empList) {
			Employee restoredEmployee = (Employee) e.clone();
			String resumeName = restoredEmployee.getName()+" - Restored";
			restoredEmployee.setResumeName(resumeName);
			//genericService.save(restoredEmployee);
		}
	}
    return "redirect:/";
} 

@RequestMapping("/modifySkills")
public String modifySkills(@ModelAttribute("employee") Employee e,@RequestParam(name="indices",required=false) List<Integer> indices,@RequestParam("action") String action,RedirectAttributes ra){
	if(action!=null) {
		List<Skill> skills = e.getSkills();
		if(skills==null)
			skills=new ArrayList<Skill>();
		Skill s=new Skill();
		if(!CollectionUtils.isEmpty(indices) && action.equals("delete"))
			Utils.removeElements(skills, indices);
		if(action.equals("add"))
			skills.add(s);
		e.setSkills(skills);
	}
	ra.addFlashAttribute("employee", e);
    return "redirect:/";
} 

@RequestMapping("/modifyProject")
public String modifyProject(@ModelAttribute("employee") Employee e,@RequestParam("companyIndex") Integer companyIndex,@RequestParam("projectIndex") Integer projectIndex,@RequestParam("action") String action,RedirectAttributes ra){
	if(companyIndex!=null && projectIndex!=null && action!=null) {
		List<Project> projects=e.getCompanies().get(companyIndex).getProjects();
		Project p = projects.get(projectIndex);
		if(action.equals("delete")) {
			projects.remove(p);
		}
		if(action.equals("duplicate")) {
			Project p1=(Project) p.clone();
			projects.add(p1);
		}
		e.getCompanies().get(companyIndex).setProjects(projects);
	}
	ra.addFlashAttribute("employee", e);
    return "redirect:/";
} 

@RequestMapping("/duplicateField")
public String duplicateField(@ModelAttribute("employee") Employee e,@RequestParam("ids") List<Byte> ids,@RequestParam("field") String field,RedirectAttributes ra){
    if(!CollectionUtils.isEmpty(ids) && field!=null) {
    	if(field.equals("companies")) {
    		List<Company> companies = e.getCompanies();
    		List<Company> companiesToBeDuplicated = new ArrayList<Company>();
    		for(byte i=0;i<companies.size();i++)
    			if(ids.contains(i))
    				companiesToBeDuplicated.add(companies.get(i));
    		List<Company> copiedCompanies = (List<Company>) Utils.COPY_LIST.apply(companiesToBeDuplicated);
    		companies.addAll(copiedCompanies);
    		e.setCompanies(companies);
    	}
    }
    ra.addFlashAttribute("employee", e);
    return "redirect:/";
} 

@RequestMapping("/duplicate")
public String duplicate(@RequestParam("ids") List<Long> ids){
    if(!CollectionUtils.isEmpty(ids)) {
    	for(Long id:ids) {
    		Employee e = new Employee();
    		Employee e2 = (Employee) e.clone();
    		//genericService.save(e2);
    	}
    }
    return "redirect:/";
}

@RequestMapping("/addMore")
public String addMore(@ModelAttribute("employee") Employee e,@RequestParam(name="field",required=true) String field,@RequestParam(name="companyIndex",required=false)Integer companyIndex,RedirectAttributes ra){
	List<Company> companies = e.getCompanies();
	Company c = new Company();
	if(companies==null)
		companies = new ArrayList<Company>();
	Project p = new Project();
	if(field.equals("companies")) {
		companies.add(c);
		e.setCompanies(companies);
	}
	if(field.equals("projects")) {
		for(int ci=0;ci<companies.size();ci++)
			if(ci==companyIndex) {
				Company cp = companies.get(ci);
				List<Project> projects = cp.getProjects();
				if(projects==null)
					projects=new ArrayList<Project>();
				projects.add(p);
				cp.setProjects(projects);
				e.setCompanies(companies);
			}
	}
	ra.addFlashAttribute("employee", e);
    return "redirect:/";
} 

@RequestMapping("/edit/{eid}")
public String edit(@PathVariable Long eid,RedirectAttributes ra){
    Employee e = (eid==null)?new Employee():new Employee();
    ra.addFlashAttribute("employee", e);
    return "redirect:/";
} 

@RequestMapping("/otherDetails/{eid}")
public ModelAndView otherDetails(@PathVariable Long eid, ModelMap m){
    Employee e = (eid==null)?new Employee():new Employee();
    m.put("o", new OtherDetails(e));
    return new ModelAndView("otherDetails",m);
} 


@RequestMapping("/save")
public ModelAndView save(@ModelAttribute("employee")Employee e, ModelMap m){
    System.out.print("\nsave : \nname : "+e+"\n");
	//genericService.save(e);
    return new ModelAndView("redirect:/",m);
}  

@RequestMapping("/showSavedDetails")
public ModelAndView showSavedDetails(@ModelAttribute("employee")Employee e, @RequestParam(name="deletedIds",required=false) List<Long> deletedIds, ModelMap m){
	List<Employee> employees = new ArrayList<Employee>();
	m.put("employees", employees);
    return new ModelAndView("savedDetails",m);
} 

static void p(Object o) {
	System.out.print("\n" + o + "\n");
}

}

