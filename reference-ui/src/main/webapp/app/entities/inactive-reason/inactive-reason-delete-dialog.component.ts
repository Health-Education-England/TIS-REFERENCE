import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {InactiveReason} from "./inactive-reason.model";
import {InactiveReasonPopupService} from "./inactive-reason-popup.service";
import {InactiveReasonService} from "./inactive-reason.service";

@Component({
	selector: 'jhi-inactive-reason-delete-dialog',
	templateUrl: './inactive-reason-delete-dialog.component.html'
})
export class InactiveReasonDeleteDialogComponent {

	inactiveReason: InactiveReason;

	constructor(private jhiLanguageService: JhiLanguageService,
				private inactiveReasonService: InactiveReasonService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['inactiveReason']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.inactiveReasonService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'inactiveReasonListModification',
				content: 'Deleted an inactiveReason'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-inactive-reason-delete-popup',
	template: ''
})
export class InactiveReasonDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private inactiveReasonPopupService: InactiveReasonPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.inactiveReasonPopupService
				.open(InactiveReasonDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
