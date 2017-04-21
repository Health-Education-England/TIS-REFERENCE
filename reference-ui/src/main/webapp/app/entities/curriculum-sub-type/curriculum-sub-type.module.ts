import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	CurriculumSubTypeService,
	CurriculumSubTypePopupService,
	CurriculumSubTypeComponent,
	CurriculumSubTypeDetailComponent,
	CurriculumSubTypeDialogComponent,
	CurriculumSubTypePopupComponent,
	CurriculumSubTypeDeletePopupComponent,
	CurriculumSubTypeDeleteDialogComponent,
	curriculumSubTypeRoute,
	curriculumSubTypePopupRoute
} from "./";

let ENTITY_STATES = [
	...curriculumSubTypeRoute,
	...curriculumSubTypePopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		CurriculumSubTypeComponent,
		CurriculumSubTypeDetailComponent,
		CurriculumSubTypeDialogComponent,
		CurriculumSubTypeDeleteDialogComponent,
		CurriculumSubTypePopupComponent,
		CurriculumSubTypeDeletePopupComponent,
	],
	entryComponents: [
		CurriculumSubTypeComponent,
		CurriculumSubTypeDialogComponent,
		CurriculumSubTypePopupComponent,
		CurriculumSubTypeDeleteDialogComponent,
		CurriculumSubTypeDeletePopupComponent,
	],
	providers: [
		CurriculumSubTypeService,
		CurriculumSubTypePopupService,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceCurriculumSubTypeModule {
}
