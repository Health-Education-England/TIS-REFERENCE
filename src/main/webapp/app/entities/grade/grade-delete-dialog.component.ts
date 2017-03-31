import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {Grade} from "./grade.model";
import {GradePopupService} from "./grade-popup.service";
import {GradeService} from "./grade.service";

@Component({
	selector: 'jhi-grade-delete-dialog',
	templateUrl: './grade-delete-dialog.component.html'
})
export class GradeDeleteDialogComponent {

	grade: Grade;

	constructor(private jhiLanguageService: JhiLanguageService,
				private gradeService: GradeService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['grade']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.gradeService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'gradeListModification',
				content: 'Deleted an grade'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-grade-delete-popup',
	template: ''
})
export class GradeDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private gradePopupService: GradePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.gradePopupService
				.open(GradeDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
