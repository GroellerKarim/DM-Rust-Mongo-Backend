use mongodb::bson::{oid::ObjectId, Timestamp};
use serde::{Serialize, Deserialize};


#[derive(Debug, Serialize, Deserialize)]
pub struct Subject {
    pub id: Option<ObjectId>,
    pub version: String,
    pub name: String,

    pub possible_examiners: Vec<Examiner>,
    pub duration: Vec<Duration>,
    pub date: Vec<Timestamp>,

    pub exam_type: String
}

#[derive(Debug, Serialize, Deserialize)]
pub struct Examiner {
    pub name: String,
    pub does_examination: bool,
    pub timestamp: Timestamp
}

#[derive(Debug, Serialize, Deserialize)]
pub struct Duration {
    pub hours: i8,
    pub minutes: i8,
    pub timestamp: Timestamp
}