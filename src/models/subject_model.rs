use mongodb::bson::{oid::ObjectId, DateTime};
use serde::{Deserialize, Serialize};

#[derive(Debug, Serialize, Deserialize)]
pub struct Subject {
    #[serde(rename = "_id", skip_serializing_if = "Option::is_none")]
    pub id: Option<ObjectId>,

    pub version: DateTime,
    pub name: String,

    pub possible_examiners: Vec<Examiner>,
    pub duration: Vec<Duration>,
    pub date: Vec<DateTime>,

    pub exam_type: String,
}

#[derive(Debug, Serialize, Deserialize)]
pub struct Examiner {
    pub name: String,
    pub does_examination: bool,
    pub timestamp: DateTime,
}

#[derive(Debug, Serialize, Deserialize)]
pub struct Duration {
    pub hours: i8,
    pub minutes: i8,
    pub timestamp: DateTime,
}
