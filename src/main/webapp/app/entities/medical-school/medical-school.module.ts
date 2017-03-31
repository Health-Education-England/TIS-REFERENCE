import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	MedicalSchoolService,
	MedicalSchoolPopupService,
	MedicalSchoolComponent,
	MedicalSchoolDetailComponent,
	MedicalSchoolDialogComponent,
	MedicalSchoolPopupComponent,
	MedicalSchoolDeletePopupComponent,
	MedicalSchoolDeleteDialogComponent,
	medicalSchoolRoute,
	medicalSchoolPopupRoute,
	MedicalSchoolResolvePagingParams
} from "./";

let ENTITY_STATES = [
	...medicalSchoolRoute,
	...medicalSchoolPopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		MedicalSchoolComponent,
		MedicalSchoolDetailComponent,
		MedicalSchoolDialogComponent,
		MedicalSchoolDeleteDialogComponent,
		MedicalSchoolPopupComponent,
		MedicalSchoolDeletePopupComponent,
	],
	entryComponents: [
		MedicalSchoolComponent,
		MedicalSchoolDialogComponent,
		MedicalSchoolPopupComponent,
		MedicalSchoolDeleteDialogComponent,
		MedicalSchoolDeletePopupComponent,
	],
	providers: [
		MedicalSchoolService,
		MedicalSchoolPopupService,
		MedicalSchoolResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceMedicalSchoolModule {
}
