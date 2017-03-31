import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	AssessmentTypeService,
	AssessmentTypePopupService,
	AssessmentTypeComponent,
	AssessmentTypeDetailComponent,
	AssessmentTypeDialogComponent,
	AssessmentTypePopupComponent,
	AssessmentTypeDeletePopupComponent,
	AssessmentTypeDeleteDialogComponent,
	assessmentTypeRoute,
	assessmentTypePopupRoute
} from "./";

let ENTITY_STATES = [
	...assessmentTypeRoute,
	...assessmentTypePopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		AssessmentTypeComponent,
		AssessmentTypeDetailComponent,
		AssessmentTypeDialogComponent,
		AssessmentTypeDeleteDialogComponent,
		AssessmentTypePopupComponent,
		AssessmentTypeDeletePopupComponent,
	],
	entryComponents: [
		AssessmentTypeComponent,
		AssessmentTypeDialogComponent,
		AssessmentTypePopupComponent,
		AssessmentTypeDeleteDialogComponent,
		AssessmentTypeDeletePopupComponent,
	],
	providers: [
		AssessmentTypeService,
		AssessmentTypePopupService,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceAssessmentTypeModule {
}
