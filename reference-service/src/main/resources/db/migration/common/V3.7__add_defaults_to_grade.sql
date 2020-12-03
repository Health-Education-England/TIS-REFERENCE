ALTER TABLE `Grade`
MODIFY `trainingGrade` bit(1) NOT NULL DEFAULT FALSE;

ALTER TABLE `Grade`
MODIFY `postGrade` bit(1) NOT NULL DEFAULT FALSE;

ALTER TABLE `Grade`
MODIFY `placementGrade` bit(1) NOT NULL DEFAULT FALSE;

CREATE UNIQUE INDEX grade_intrepid_id ON `Grade` (`intrepidId`);
