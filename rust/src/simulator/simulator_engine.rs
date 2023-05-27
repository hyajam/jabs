use crate::simulator::event::Event;
use priority_queue::PriorityQueue;
use std::cell::RefCell;
use std::cmp::Ordering;
use std::hash::{Hash, Hasher};
use std::rc::Rc;

///  @author arash & habib:
/// A discrete time event-based simulator with event queue
#[derive(Default)]
pub struct Simulator {
    /// The queue that contains all events which are going to be executed. This
    /// queue is a priority queue sorted by the time in which the event should
    /// be executed.
    ///
    /// The `event_queue` is of type [`PriorityQueue`], including events of type
    /// [`ScheduledEvent`].  
    event_queue: PriorityQueue<ScheduledEvent, ScheduledEvent>,
    /// The simulation execution time of the most recent event
    simulation_time: f64,
    /// Number of events inserted in the event queue till now (whether simulated
    /// or not)
    inserted_events: i64,
}

impl Simulator {
    pub fn new() -> Self {
        Self {
            event_queue: PriorityQueue::new(),
            simulation_time: 0.0,
            inserted_events: 0,
        }
    }

    /// Executes the next event in the event queue.
    pub fn execute_next_event(&mut self) {
        if let Some((current_scheduled_event, _)) = self.event_queue.pop() {
            self.simulation_time = current_scheduled_event.time;
            println!("simulation time: {}", self.simulation_time);
            current_scheduled_event.event.borrow().execute()
        }
    }

    /// Returns what is the next event to be executed without executing the
    /// event.
    ///
    /// # Returns
    ///
    /// The next event to be executed in the simulator
    pub fn peek_event(&self) -> Option<Rc<RefCell<dyn Event>>> {
        self.event_queue.peek().map(|(se, _)| se.event.clone())
        // self.event_queue.peek().map(|(se, _)| Rc::clone(&se.event))
    }

    /// Check if more events exist in the event queue to be simulated.
    ///
    /// # Returns
    ///
    /// true if there is any event in the queue
    pub fn is_there_more_events(&self) -> bool {
        !self.event_queue.is_empty()
    }

    /// Inserts a new event in event queue. The event execution time will be the
    /// summation of current time and remaining time to execution.
    ///
    /// # Arguments
    ///
    /// * `event`: The event to be executed
    /// * `remaining_time_to_execution`: The time remaining to execution time of
    /// the event.
    ///
    /// # Returns
    ///
    /// the scheduled event
    pub fn put_event(
        &mut self,
        event: Rc<RefCell<dyn Event>>,
        remaining_time_to_execution: f64,
    ) -> ScheduledEvent {
        let s_event = ScheduledEvent::new(
            event,
            self.simulation_time + remaining_time_to_execution,
            self.inserted_events,
        );
        self.event_queue.push(s_event.clone(), s_event.clone());
        self.inserted_events += 1;
        s_event
    }

    /// Removes an event already available in the event queue. It is specially
    /// useful for processes that are ongoing such as packet receiving process
    /// or block mining process.
    ///
    /// # Arguments
    ///
    /// * `scheduled_event`: The  scheduled event to be removed
    pub fn remove_event(&mut self, scheduled_event: ScheduledEvent) {
        self.event_queue.remove(&scheduled_event);
    }

    /// Returns the simulation time that the latest event has executed.
    ///
    /// # Returns
    ///
    /// Simulation time of the latest simulated event
    pub fn get_simulation_time(&self) -> f64 {
        self.simulation_time
    }

    /// Clears the event queue from any more events. Restarts the current time
    /// of simulation to zero.
    pub fn reset(&mut self) {
        self.event_queue.clear();
        self.simulation_time = 0.0;
    }
}

/// The scheduled event struct, including the event, it's time , and the id
/// number
#[derive(Clone, Debug)]
pub struct ScheduledEvent {
    /// The event which must implement [`Event`] trait
    event: Rc<RefCell<dyn Event>>,
    /// Simulation execution time of the event
    time: f64,
    /// Event ID (insertion number in event queue)
    number: i64,
}

impl Hash for ScheduledEvent {
    fn hash<H: Hasher>(&self, state: &mut H) {
        self.number.hash(state);
    }
}

impl PartialEq for ScheduledEvent {
    fn eq(&self, other: &Self) -> bool {
        (self.number == other.number) && (self.time == other.time)
    }
}

impl Eq for ScheduledEvent {}

impl PartialOrd for ScheduledEvent {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        Some(self.cmp(other))
    }
}

/// This function is used by the priority queue to sort the scheduled events
/// It first sorts the event such the event which has min time will receive the
/// most priority. If two events have equal execution time, then their id number
/// decides who executes first, by giving priority to the one which is added
/// sooner to the queue (less id number).
impl Ord for ScheduledEvent {
    fn cmp(&self, other: &Self) -> Ordering {
        match self.time {
            x if x < other.time => Ordering::Greater,
            x if x > other.time => Ordering::Less,
            x if x == other.time => other.number.cmp(&self.number),
            _ => Ordering::Equal,
        }
    }
}

impl Event for ScheduledEvent {}

impl ScheduledEvent {
    //TODO: remove the trait object Rc<RefCell<dyn Event>>.
    pub fn new(event: Rc<RefCell<dyn Event>>, time: f64, number: i64) -> Self {
        ScheduledEvent {
            event,
            time,
            number,
        }
    }

    /// Returns the corresponding event.
    pub fn event(&self) -> Rc<RefCell<dyn Event>> {
        self.event.clone()
    }

    /// Returns the execution time of the event.
    pub fn time(&self) -> f64 {
        self.time
    }

    /// Returns the event ID.
    pub fn number(&self) -> i64 {
        self.number
    }
}
