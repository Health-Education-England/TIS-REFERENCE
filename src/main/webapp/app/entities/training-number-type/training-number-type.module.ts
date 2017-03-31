import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	TrainingNumberTypeService,
	TrainingNumberTypePopupService,
	TrainingNumberTypeComponent,
	TrainingNumberTypeDetailComponent,
	TrainingNumberTypeDialogComponent,
	TrainingNumberTypePopupComponent,
	TrainingNumberTypeDeletePopupComponent,
	TrainingNumberTypeDeleteDialogComponent,
	trainingNumberTypeRoute,
	trainingNumberTypePopupRoute
} from "./";

let ENTITY_STATES = [
	...trainingNumberTypeRoute,
	...trainingNumberTypePopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		TrainingNumberTypeComponent,
		TrainingNumberTypeDetailComponent,
		TrainingNumberTypeDialogComponent,
		TrainingNumberTypeDeleteDialogComponent,
		TrainingNumberTypePopupComponent,
		TrainingNumberTypeDeletePopupComponent,
	],
	entryComponents: [
		TrainingNumberTypeComponent,
		TrainingNumberTypeDialogComponent,
		TrainingNumberTypePopupComponent,
		TrainingNumberTypeDeleteDialogComponent,
		TrainingNumberTypeDeletePopupComponent,
	],
	providers: [
		TrainingNumberTypeService,
		TrainingNumberTypePopupService,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceTrainingNumberTypeModule {
}
