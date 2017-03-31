import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {LocalOffice} from "./local-office.model";
import {LocalOfficePopupService} from "./local-office-popup.service";
import {LocalOfficeService} from "./local-office.service";

@Component({
	selector: 'jhi-local-office-delete-dialog',
	templateUrl: './local-office-delete-dialog.component.html'
})
export class LocalOfficeDeleteDialogComponent {

	localOffice: LocalOffice;

	constructor(private jhiLanguageService: JhiLanguageService,
				private localOfficeService: LocalOfficeService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['localOffice']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.localOfficeService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'localOfficeListModification',
				content: 'Deleted an localOffice'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-local-office-delete-popup',
	template: ''
})
export class LocalOfficeDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private localOfficePopupService: LocalOfficePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.localOfficePopupService
				.open(LocalOfficeDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
