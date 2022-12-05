use mongodb::{bson::DateTime, results::{InsertOneResult, UpdateResult}, Cursor};
use rocket::{http::Status, log::private::logger, serde::json::Json, State};

use crate::{
    models::subject_model::{CreateSubjectDto, Subject, AddFullExamDto},
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

/*#[put("/subject/add/exam", data = "<new_exam>")]
pub fn add_full_exam(db: &State<MongoRepo>, new_exam: Json<AddFullExamDto>) -> Result<Json<Subject>, Status> {
    let new_data_string = stringify!(new_exam);

    let id = new_exam.id;
    let examDto = AddFullExamDto {
        id : new_exam.id,

        examiner_name: new_exam.examiner_name,
        does_examination: new_exam.does_examination,

        hours: new_exam.hours,
        minutes: new_exam.minutes,

        exam_type: new_exam.exam_type
    };

    let subject_detail = db.add_full_exam(&examDto);

    match subject_detail {
        Ok(subject) => {
            let updated_subject_info = db.get_subject_by_id(&id);

            return match updated_subject_info {
                Ok(s) => Ok(Json(s)),
                Err(_) => Err(Status::InternalServerError),
            };
        },
        Err(_) => Err(Status::InternalServerError)
    }
}
*/

#[delete("/subject/delete/<id>")]
pub fn delete_subject(db: &State<MongoRepo>, id: String) -> Result<Json<&str>, Status> {
    if id.is_empty() {
        return Err(Status::BadRequest);
    }

    let subject_detail = db.delete_subject(&id);

    match subject_detail {
        Ok(subject) => {
            if subject.deleted_count == 1 {
                return Ok(Json("Subject successfully deleted!"));
            } else {
                return Err(Status::NotFound);
            }
        }
        Err(_) => Err(Status::InternalServerError),
    }
}
