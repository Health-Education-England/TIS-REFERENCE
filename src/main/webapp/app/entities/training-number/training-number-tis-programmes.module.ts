import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	TrainingNumberTisProgrammesService,
	TrainingNumberTisProgrammesPopupService,
	TrainingNumberTisProgrammesComponent,
	TrainingNumberTisProgrammesDetailComponent,
	TrainingNumberTisProgrammesDialogComponent,
	TrainingNumberTisProgrammesPopupComponent,
	TrainingNumberTisProgrammesDeletePopupComponent,
	TrainingNumberTisProgrammesDeleteDialogComponent,
	trainingNumberRoute,
	trainingNumberPopupRoute
} from "./";

let ENTITY_STATES = [
	...trainingNumberRoute,
	...trainingNumberPopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		TrainingNumberTisProgrammesComponent,
		TrainingNumberTisProgrammesDetailComponent,
		TrainingNumberTisProgrammesDialogComponent,
		TrainingNumberTisProgrammesDeleteDialogComponent,
		TrainingNumberTisProgrammesPopupComponent,
		TrainingNumberTisProgrammesDeletePopupComponent,
	],
	entryComponents: [
		TrainingNumberTisProgrammesComponent,
		TrainingNumberTisProgrammesDialogComponent,
		TrainingNumberTisProgrammesPopupComponent,
		TrainingNumberTisProgrammesDeleteDialogComponent,
		TrainingNumberTisProgrammesDeletePopupComponent,
	],
	providers: [
		TrainingNumberTisProgrammesService,
		TrainingNumberTisProgrammesPopupService,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceTrainingNumberTisProgrammesModule {
}
