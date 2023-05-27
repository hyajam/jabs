use std::fmt::Debug;

pub trait Event: Debug {
    fn execute(&self) {}
}
