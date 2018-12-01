package meisterTask.controllers;

import meisterTask.bindingModel.TaskBindingModel;
import meisterTask.entities.Task;
import meisterTask.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class TaskController {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/")
    public ModelAndView index(ModelAndView modelAndView) {

      List<Task> openTasks = taskRepository.findAllByStatus("Open");
      List<Task> inProgress = taskRepository.findAllByStatus("In Progress");
      List<Task> finishedTasks = taskRepository.findAllByStatus("Finished");

      modelAndView.setViewName("base-layout");
      modelAndView.addObject("view", "task/index");

      modelAndView.addObject("openTasks", openTasks);
      modelAndView.addObject("inProgressTasks", inProgress);
      modelAndView.addObject("finishedTasks", finishedTasks);

        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView create(ModelAndView modelAndView) {
       modelAndView.setViewName("base-layout");
       modelAndView.addObject("view", "task/create");

        return modelAndView;
    }

    @PostMapping("/create")
    public String create(Task tasks) {
      taskRepository.saveAndFlush(tasks);

        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(ModelAndView modelAndView,
                             @PathVariable(value = "id") Integer id) {
        Task taskToEdit = taskRepository.getOne(id);
        modelAndView.setViewName("base-layout");
        modelAndView.addObject("view", "task/edit");
        modelAndView.addObject("task", taskToEdit);

        return modelAndView;
    }

    @PostMapping("/edit/{id}")
    public String edit(TaskBindingModel taskBindingModel,
                       @PathVariable(value = "id") Integer id) {
        Task taskToEdit = taskRepository.getOne(id);
        taskToEdit.setTitle(taskBindingModel.getTitle());
        taskToEdit.setStatus(taskBindingModel.getStatus());
        taskRepository.saveAndFlush(taskToEdit);

       return "redirect:/";
    }
    @GetMapping("/delete/{id}")
    public ModelAndView delete(ModelAndView modelAndView,
                             @PathVariable(value = "id") Integer id) {

        Task taskToDelite = taskRepository.getOne(id);
       modelAndView.setViewName("base-layout");
       modelAndView.addObject("view", "task/delete");
       modelAndView.addObject("task",taskToDelite);

        return modelAndView;
    }

    @PostMapping("/delete/{id}")
    public String delete(Task task) {

       taskRepository.delete(task);

        return "redirect:/";
    }
}
