package tacos.web;
// Это контроллер для создания заказа
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import tacos.TacoOrder;
import tacos.data.OrderRepository;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/orders") // по этому адресу будут храниться все заказы
@SessionAttributes("tacoOrder")
public class OrderController{
    private OrderRepository orderRepo;

    public OrderController(OrderRepository orderRepo){
        this.orderRepo = orderRepo;
    } // связываем репозиторий заказа
    @GetMapping("/current") // Этот метод будет принимать GET запрос по адресу /orders/current
    public String orderForm(){
        return "orderForm"; // использования представления orderForm
    }
    @PostMapping // Это обработка запроса POST по
    public String processOrder(@Valid TacoOrder order, Errors errors, SessionStatus sessionStatus) { // тут в параметрах order который будет отправлен из представления orderForm
        if(errors.hasErrors()){
            return "orderForm";
        }
        orderRepo.save(order); // сохраняем заказ
        log.info("Order submitted: {}",order);
        sessionStatus.setComplete(); // это нужно, чтобы очистить текущий заказ тако, то есть говорим, что сессия закончилась
        return "redirect:/";
    }
}
