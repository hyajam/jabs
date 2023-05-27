use crate::simulator::event::Event;
use crate::simulator::Simulator;
use std::cell::RefCell;
use std::rc::Rc;

#[test]
fn simulator_engine_test1() {
    #[derive(Debug)]
    struct MyEvent {
        message: String,
    }

    impl Event for MyEvent {
        fn execute(&self) {
            println!("{}", self.message);
        }
    }

    impl MyEvent {
        fn new(message: String) -> Self {
            Self { message }
        }
    }

    let mut sim = Simulator::new();

    // Defining events with arbitrary time differences
    let no_of_events = 3;
    let time_diff = vec![9.0, 3.0, 5.27];
    let messages: Vec<String> = (0..no_of_events)
        .zip(time_diff.iter())
        .map(|(event_number, time)| {
            format!(
                "Executing Event {} with the time difference {}",
                event_number + 1,
                time
            )
        })
        .collect();

    let events: Vec<_> = (0..no_of_events)
        .map(|i| Rc::new(RefCell::new(MyEvent::new(messages[i].clone()))))
        .collect();

    // Add events into the simulator queue.
    for i in 0..no_of_events {
        sim.put_event(events[i].clone(), time_diff[i]);
    }

    // get_simulation_time and execute_next_event tests
    let mut sorted_time_diff = time_diff;
    sorted_time_diff.sort_by(|a, b| a.partial_cmp(b).unwrap());
    let mut ctr = 0;
    while sim.is_there_more_events() {
        sim.execute_next_event();
        assert_eq!(sim.get_simulation_time(), sorted_time_diff[ctr]);
        ctr += 1;
    }

    // reset test
    sim.reset();
    assert_eq!(sim.get_simulation_time(), 0.0_f64);

    // another scenario:
    println!();
    println!("-------------");
    println!("new scenario:");
    println!("-------------");
    sim.put_event(
        Rc::new(RefCell::new(MyEvent::new(String::from(
            "event 1, added at=0, diff time=31",
        )))),
        31.0_f64,
    );
    sim.put_event(
        Rc::new(RefCell::new(MyEvent::new(String::from(
            "event 2, added at=0, diff time=9",
        )))),
        9.0_f64,
    );
    sim.execute_next_event();
    assert_eq!(sim.get_simulation_time(), 9.0_f64);
    sim.put_event(
        Rc::new(RefCell::new(MyEvent::new(String::from(
            "event 3, added at=9, diff time=10",
        )))),
        10.0_f64,
    );
    sim.execute_next_event();
    assert_eq!(sim.get_simulation_time(), 19.0_f64);
    sim.put_event(
        Rc::new(RefCell::new(MyEvent::new(String::from(
            "event 4, added at=19, diff time=12",
        )))),
        12.0_f64,
    );
    sim.execute_next_event();
    assert_eq!(sim.get_simulation_time(), 31.0_f64);
    sim.execute_next_event();
    assert_eq!(sim.get_simulation_time(), 31.0_f64);
}
