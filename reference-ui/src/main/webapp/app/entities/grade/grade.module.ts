import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	GradeService,
	GradePopupService,
	GradeComponent,
	GradeDetailComponent,
	GradeDialogComponent,
	GradePopupComponent,
	GradeDeletePopupComponent,
	GradeDeleteDialogComponent,
	gradeRoute,
	gradePopupRoute
} from "./";

let ENTITY_STATES = [
	...gradeRoute,
	...gradePopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		GradeComponent,
		GradeDetailComponent,
		GradeDialogComponent,
		GradeDeleteDialogComponent,
		GradePopupComponent,
		GradeDeletePopupComponent,
	],
	entryComponents: [
		GradeComponent,
		GradeDialogComponent,
		GradePopupComponent,
		GradeDeleteDialogComponent,
		GradeDeletePopupComponent,
	],
	providers: [
		GradeService,
		GradePopupService,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceGradeModule {
}
