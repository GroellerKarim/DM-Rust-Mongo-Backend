use mongodb::{bson::DateTime, results::InsertOneResult, Cursor};
use rocket::{http::Status, log::private::logger, serde::json::Json, State};

use crate::{
    models::subject_model::{CreateSubjectDto, Subject},
    repository::mondodb_repo::MongoRepo,
};

#[post("/subject", data = "<new_subject>")]
pub fn create_subject(
    db: &State<MongoRepo>,
    new_subject: Json<CreateSubjectDto>,
) -> Result<Json<InsertOneResult>, Status> {
    let new_data_string = stringify!(new_subject);
    println!("{}", new_data_string);

    let date = DateTime::now();
    let data = Subject {
        id: None,
        version: date.to_owned(),
        name: new_subject.name.to_owned(),
        exams: vec![],
    };

    let data_string = stringify!(data);
    println!("{}", data_string);

    let subject_detail = db.create_subject(data);

    match subject_detail {
        Ok(subject) => Ok(Json(subject)),
        Err(_) => Err(Status::InternalServerError),
    }
}

#[get("/subjects")]
pub fn get_all_subjects(db: &State<MongoRepo>) -> Result<Json<Vec<Subject>>, Status> {
    let subjects = db.get_all_subjects();

    match subjects {
        Ok(subjects) => Ok(Json(subjects)),
        Err(subjects) => Err(Status::InternalServerError),
    }
}

#[get("/subject/<id>")]
pub fn get_subject_by_id(db: &State<MongoRepo>, id: String) -> Result<Json<Subject>, Status> {
    if id.is_empty() {
        return Err(Status::BadRequest);
    };

    let subject_detail = db.get_subject_by_id(&id);

    match subject_detail {
        Ok(subject) => Ok(Json(subject)),
        Err(_) => Err(Status::InternalServerError),
    }
}
